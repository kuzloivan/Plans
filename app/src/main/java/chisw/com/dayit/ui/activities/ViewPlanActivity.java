package chisw.com.dayit.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Random;

import chisw.com.dayit.R;
import chisw.com.dayit.core.PApplication;
import chisw.com.dayit.db.DBManager;
import chisw.com.dayit.db.entity.PlansEntity;
import chisw.com.dayit.model.Plan;
import chisw.com.dayit.others.Multimedia;
import chisw.com.dayit.ui.dialogs.TwoButtonsAlertDialog;
import chisw.com.dayit.utils.BitmapUtils;
import chisw.com.dayit.utils.DataUtils;
import chisw.com.dayit.utils.SystemUtils;

public class ViewPlanActivity extends ToolbarActivity {

    public static final String BUNDLE_ID_KEY = "chisw.com.DayIt.ui.activities.view_plan_activity.id";
    public static final String BUNDLE_KEY = "chisw.com.DayIt.ui.activities.view_plan_activity.bundle";

    private TextView mTv_time;
    private TextView mTv_date;
    private TextView mTv_details;
    public ImageView mIvPicture;
    private Plan mPlan;
    private int mPlanId;
    private String mSelectedImagePath;
    private Picasso mPicasso;

    public static void start(Activity pActivity, int pId) {
        Intent intent = new Intent(pActivity, ViewPlanActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_ID_KEY, pId);
        intent.putExtra(BUNDLE_KEY, bundle);
        pActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle pSavedInstanceState) {
        super.onCreate(pSavedInstanceState);
        initView();
        initBackButton();
        mPicasso = Picasso.with(this);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        showToast("onResume");
//        initPicture();
//    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        initPicture();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu pMenu) {
        getMenuInflater().inflate(R.menu.menu_view_plan, pMenu);
        return super.onCreateOptionsMenu(pMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem pMenuItem) {
        switch (pMenuItem.getItemId()) {
            case R.id.vp_menu_delete:
                TwoButtonsAlertDialog dialDelPlan = new TwoButtonsAlertDialog();
                dialDelPlan.setIAlertDialog(new DeletePlanDialogClicker());
                dialDelPlan.setDialogTitle("Are you sure you want to delete this plan?");
                dialDelPlan.setPositiveBtnText("Yes, I'm sure");
                dialDelPlan.setNegativeBtnText("No, I'm not");
                dialDelPlan.show(getFragmentManager(), getString(R.string.pa_delete_plan));
                break;
            case R.id.vp_menu_edit:
                if (mPlan.getIsRemote() == 0) {
                    LocalTaskActivity.start(this, mPlanId);
                }
                if (mPlan.getIsRemote() == 1) {
                    RemoteTaskActivity.start(this, mPlanId);
                }
                finish();
                break;
        }
        return super.onOptionsItemSelected(pMenuItem);
    }

    public void deleteEntirely() {
        alarmManager.cancelAlarm(mPlanId);
        Plan plan = dbManager.getPlanById(mPlanId);
        if (sharedHelper.getDefaultLogin().isEmpty() || !sharedHelper.getSynchronization() || !SystemUtils.checkNetworkStatus(getApplicationContext())) {
            plan.setIsSynchronized(0);
            plan.setIsDeleted(1);
            dbManager.editPlan(plan, mPlanId);
        } else {
            netManager.deletePlan(plan.getParseId());
            dbManager.deletePlanById(mPlanId);
            plan.setIsSynchronized(1);
        }
        finish();
    }

    private void initPicture() {
        int targetW = mIvPicture.getWidth();
        int targetH = mIvPicture.getHeight();


        if (mSelectedImagePath!=null){
            Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(mSelectedImagePath, targetW, targetH);
            mIvPicture.setImageBitmap(bitmap);
        }else{
            int color = Color.argb(255,63,81,181);
            mIvPicture.setBackgroundColor(color);
        }

// Don't delete
//        try {
//            Uri imageUri;
//            imageUri = Uri.fromFile(new File(mSelectedImagePath));
//            String imageUriString = imageUri.toString();
//            mPicasso.load(imageUriString).resize(targetW, targetH).centerCrop().into(mIvPicture);
//
//
//        } catch (Exception e) {
//
//            mPicasso.load(R.drawable.default_example_material).centerCrop().into(mIvPicture);
////            mIvPicture.setImageResource(R.drawable.default_example_material);
//        }
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_view_plan;
    }

    private void initView() {

        mPlanId = getIntent().getBundleExtra(BUNDLE_KEY).getInt(BUNDLE_ID_KEY);
        mPlan = dbManager.getPlanById(mPlanId);
        mSelectedImagePath = mPlan.getImagePath();
        setTitle(mPlan.getTitle());
        mTv_time = (TextView) findViewById(R.id.pv_tv_time);
        mTv_date = (TextView) findViewById(R.id.pv_tv_date);
        mTv_details = (TextView) findViewById(R.id.pv_tv_details);
        mIvPicture = (ImageView) findViewById(R.id.image_view_on_toolbar);
        mTv_time.setText(DataUtils.getTimeStringFromTimeStamp(mPlan.getTimeStamp()));

        if(mPlan.getDaysToAlarm().charAt(0) == '1'){
            mTv_date.setText(DataUtils.getDaysForRepeatingFromString(mPlan.getDaysToAlarm()));
        }
        else {
            mTv_date.setText(DataUtils.getDateStringFromTimeStamp(mPlan.getTimeStamp())); // todo: add if for correct InView displaying
        }

        mTv_details.setText(mPlan.getDetails());
    }
    private final class DeletePlanDialogClicker implements TwoButtonsAlertDialog.IAlertDialog {

        @Override
        public void onAcceptClick() {
            deleteEntirely();
        }
    }

}
