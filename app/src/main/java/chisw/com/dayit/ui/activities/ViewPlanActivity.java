package chisw.com.dayit.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import chisw.com.dayit.R;
import chisw.com.dayit.model.Plan;
import chisw.com.dayit.net.PushManager;
import chisw.com.dayit.ui.dialogs.TwoButtonsAlertDialog;
import chisw.com.dayit.utils.BitmapUtils;
import chisw.com.dayit.utils.DataUtils;
import chisw.com.dayit.utils.SystemUtils;
import chisw.com.dayit.utils.ValidData;

public class ViewPlanActivity extends ToolbarActivity {

    public static final String BUNDLE_ID_KEY = "chisw.com.DayIt.ui.activities.view_plan_activity.id";
    public static final String BUNDLE_KEY = "chisw.com.DayIt.ui.activities.view_plan_activity.bundle";

    private TextView mTv_time;
    private TextView mTv_date;
    private TextView mTv_details;
    private TextView mTv_sound;
    private TextView mTv_days_of_week;
    public ImageView mIvPicture;
    private Plan mPlan;
    private int mPlanId;
    private String mSelectedImagePath;
    private Picasso mPicasso;
    private Button mBtnAccept;
    private Button mBtnReject;

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
    }

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
                dialDelPlan.setPositiveBtnText("Yes");
                dialDelPlan.setNegativeBtnText("Cancel");
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

    private void logMemory(Context context) {
        Toast.makeText(context, "Total memory = " + (Runtime.getRuntime().totalMemory() / 1024), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int contentViewResId() {
        return R.layout.activity_view_plan;
    }

    private void initView() {

        Clicker clicker = new Clicker();

        mPlanId = getIntent().getBundleExtra(BUNDLE_KEY).getInt(BUNDLE_ID_KEY);
        mPlan = dbManager.getPlanById(mPlanId);
        mSelectedImagePath = mPlan.getImagePath();
        setTitle(mPlan.getTitle());
        mTv_time = (TextView) findViewById(R.id.pv_tv_time);
        mTv_details = (TextView) findViewById(R.id.pv_tv_details);
        mTv_sound = (TextView) findViewById(R.id.pv_tv_sound);
        mTv_days_of_week = (TextView) findViewById(R.id.pv_tv_days_of_week);
        mIvPicture = (ImageView) findViewById(R.id.image_view_on_toolbar);
        mTv_time.setText(DataUtils.getTimeStringFromTimeStamp(mPlan.getTimeStamp()));
        mBtnAccept = (Button) findViewById(R.id.vp_btn_accept);
        mBtnReject = (Button) findViewById(R.id.vp_btn_reject);
        if(mPlan.getPlanState().equals(Plan.PLAN_STATE_REMOTE_NOT_ACCEPTED)) {
            mBtnAccept.setOnClickListener(clicker);
            mBtnReject.setOnClickListener(clicker);
        } else {
            mBtnAccept.setVisibility(View.GONE);
            mBtnReject.setVisibility(View.GONE);
        }

       // mAcceptBT.setVisibility((mPlan.getIsRemote() == 1) ? View.VISIBLE : View.INVISIBLE); // todo: set condition for visibility

        if(mPlan.getDaysToAlarm().charAt(0) == '1'){
            mTv_days_of_week.setText(DataUtils.getDaysForRepeatingFromString(mPlan.getDaysToAlarm()));
        }
        else {
            mTv_days_of_week.setText(DataUtils.getDateStringFromTimeStamp(mPlan.getTimeStamp()));
        }

        mTv_details.setText(mPlan.getDetails());
        //mTv_days_of_week.setText(DataUtils.getDaysForRepeatingFromString(mPlan.getDaysToAlarm()));
        //logMemory(this);
    }

    private final class Clicker implements View.OnClickListener {

        @Override
        public void onClick(View pView) {
            switch(pView.getId()) {
                case R.id.vp_btn_accept:
                    mBtnAccept.setVisibility(View.INVISIBLE);
                    mBtnReject.setVisibility(View.INVISIBLE);
                    pushManager.sendAcceptAnswer(mPlan, sharedHelper.getDefaultLogin());
                    mPlan.setPlanState(Plan.PLAN_STATE_REMOTE_ACCEPTED);
                    dbManager.editPlan(mPlan, mPlanId);
                    showToast("Plan has been accepted");
                    break;
                case R.id.vp_btn_reject:
                    mBtnAccept.setVisibility(View.INVISIBLE);
                    mBtnReject.setVisibility(View.INVISIBLE);
                    pushManager.sendRejectAnswer(mPlan, sharedHelper.getDefaultLogin());
                    mPlan.setPlanState(Plan.PLAN_STATE_REMOTE_REJECTED);
                    dbManager.editPlan(mPlan, mPlanId);
                    showToast("Plan has been rejected");
                    break;
            }
        }
    }

    private final class DeletePlanDialogClicker implements TwoButtonsAlertDialog.IAlertDialog {

        @Override
        public void onAcceptClick() {
            deleteEntirely();
        }
    }
}
