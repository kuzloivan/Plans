package chisw.com.dayit.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import chisw.com.dayit.R;
import chisw.com.dayit.model.Plan;
import chisw.com.dayit.ui.dialogs.TwoButtonsAlertDialog;
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
                if(mPlan.getIsRemote() == 0)
                {
                    LocalTaskActivity.start(this, mPlanId);
                }
                if(mPlan.getIsRemote() == 1)
                {
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

    public static Bitmap getCircleMaskedBitmapUsingShader(Bitmap source, int radius) {

        if (source == null) {
            return null;
        }

        int diam = radius << 1;

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(source, 100, 100, true);
        final Shader shader = new BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);

        Bitmap targetBitmap = Bitmap.createBitmap(diam, diam, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);

        canvas.drawCircle(radius, radius, radius, paint);

        return targetBitmap;
    }


    @Override
    protected int contentViewResId() {
        return R.layout.activity_view_plan;
    }

    private void initView() {
        mTv_time = (TextView) findViewById(R.id.pv_tv_time);
        mTv_date = (TextView) findViewById(R.id.pv_tv_date);
        mTv_details = (TextView) findViewById(R.id.pv_tv_details);
        mIvPicture = (ImageView) findViewById(R.id.image_view_on_toolbar);
        mPlanId = getIntent().getBundleExtra(BUNDLE_KEY).getInt(BUNDLE_ID_KEY);
        mPlan = dbManager.getPlanById(mPlanId);
        setTitle(mPlan.getTitle());
        mTv_time.setText(DataUtils.getTimeStringFromTimeStamp(mPlan.getTimeStamp()));
        mTv_date.setText(DataUtils.getDateStringFromTimeStamp(mPlan.getTimeStamp()));
        mTv_details.setText(mPlan.getDetails());
    }

    private final class DeletePlanDialogClicker implements TwoButtonsAlertDialog.IAlertDialog {

        @Override
        public void onAcceptClick() {
            deleteEntirely();
        }
    }
}
