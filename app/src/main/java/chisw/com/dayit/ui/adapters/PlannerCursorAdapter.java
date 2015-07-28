package chisw.com.dayit.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Request;

import java.io.File;

import chisw.com.dayit.R;
import chisw.com.dayit.db.entity.PlansEntity;
import chisw.com.dayit.utils.DataUtils;


/**
 * Created by Alexander on 17.06.2015.
 */
public class PlannerCursorAdapter extends CursorAdapter {

    private LayoutInflater layoutInflater;
    private String mSelectedImagePath;
    private Picasso mPicasso;
    private static final String TAG = "DayIt";
    //private MyTask mt;

    public PlannerCursorAdapter(Context context) {
        super(context, null, false);
        layoutInflater = LayoutInflater.from(context);
        mPicasso = Picasso.with(context);
    }

//    class MyTask extends AsyncTask<ViewHolder, Void, Void> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            Log.i(TAG, "Start asyncTask");
//        }
//
//        @Override
//        protected Void doInBackground(ViewHolder... params) {
//
//
//            if (mSelectedImagePath!=null){
//                Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(mSelectedImagePath, 135, 204);
//                viewHolder.ivPicture.setImageBitmap(bitmap);
//            }else{
//                int color = Color.argb(255,63,81,181);
//                mIvPicture.setBackgroundColor(color);
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            Log.i(TAG, "Stop asyncTask");
//        }
//    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.planner_list_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int titleIndex = cursor.getColumnIndex(PlansEntity.TITLE);
        int detailsIndex = cursor.getColumnIndex(PlansEntity.DETAILS);//todo dont forget!
        int imageIndex = cursor.getColumnIndex(PlansEntity.IMAGE_PATH);
        long timeStamp = cursor.getLong(cursor.getColumnIndex(PlansEntity.TIMESTAMP));
        int daysIndex = cursor.getColumnIndex(PlansEntity.DAYS_TO_ALARM);
        int isRemoteIndex = cursor.getColumnIndex(PlansEntity.IS_REMOTE);
        int senderIndex = cursor.getColumnIndex(PlansEntity.SENDER);
        int planStateIndex = cursor.getColumnIndex(PlansEntity.PLAN_STATE);

        if (cursor.getInt(isRemoteIndex)==1){
            viewHolder.tvRemote.setText("remote");
        }
        else{
            viewHolder.tvRemote.setText("local");
        }
        viewHolder.tvSender.setText(cursor.getString(senderIndex));
        viewHolder.tvTitle.setText(cursor.getString(titleIndex));

        if (timeStamp < System.currentTimeMillis()){
            viewHolder.tvTime.setText("Done");
            //viewHolder.tvDate.setVisibility(View.INVISIBLE);
        }
        else {
            viewHolder.tvTime.setText(DataUtils.getTimeStringFromTimeStamp(timeStamp));
            if (cursor.getString(daysIndex).charAt(0) == '1') {
                viewHolder.tvDate.setText(DataUtils.getDaysForRepeatingFromString(cursor.getString(daysIndex)));
            } else {
                viewHolder.tvDate.setText(DataUtils.getDateStringFromTimeStamp(timeStamp));
            }
        }
        //viewHolder.tvDetails.setText(cursor.getString(detailsIndex));
        viewHolder.tvDetails.setText(cursor.getString(planStateIndex));
//        int targetW = viewHolder.ivPicture.getWidth();
//        int targetH = viewHolder.ivPicture.getHeight();
//        int targetW = 135;
//        int targetH = 204;
        mSelectedImagePath = cursor.getString(imageIndex);

//        mt = new MyTask();
//        mt.execute();

        try {
//            don't delete
//            if (mSelectedImagePath != null){
//                Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(mSelectedImagePath, targetW, targetH);
//                long redBucket = 0;
//                long greenBucket = 0;
//                long blueBucket = 0;
//                long pixelCount = 0;
//                for (int y = 1; y < bitmap.getHeight(); y++)
//                {
//                    for (int x = 1; x < bitmap.getWidth(); x++)
//                    {
//                        int c = bitmap.getPixel(x, y);
//
//                        pixelCount++;
//                        redBucket += Color.red(c);
//                        greenBucket += Color.green(c);
//                        blueBucket += Color.blue(c);
//                        // does alpha matter?
//                    }
//                }
//                int averageColor = Color.rgb((int)redBucket / (int)pixelCount, (int)greenBucket / (int)pixelCount, (int)blueBucket / (int)pixelCount);
//                viewHolder.mCardView.setCardBackgroundColor(averageColor);
//            }
            Uri imageUri;
            imageUri = Uri.fromFile(new File(mSelectedImagePath));
            String imageUriString = imageUri.toString();
            mPicasso.load(imageUriString).resizeDimen(R.dimen.plv_image_view_width, R.dimen.plv_image_view_high).centerCrop().into(viewHolder.ivPicture);
        } catch (Exception e) {
            e.printStackTrace();
//            Don't delete
//            Random rnd = new Random();
//            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//            viewHolder.ivPicture.setBackgroundColor(color);
//            int color = Color.argb(255,63,81,181);
//            viewHolder.ivPicture.setBackgroundColor(color);
            viewHolder.ivPicture.setImageResource(R.drawable.default_example_material);
        }
        Log.i(TAG, "total memory = " + Runtime.getRuntime().totalMemory() / 1024);
        Log.i(TAG, "free  memory = "+Runtime.getRuntime().freeMemory()/1024);
    }

    private static class ViewHolder {
        public TextView tvTitle;
        public TextView tvTime;
        public TextView tvDate;
        public TextView tvDetails;
        public ImageView ivPicture;
        public TextView tvRemote;
        public CardView mCardView;
        public TextView tvSender;
        public TextView tvFromForHide;
        public ViewHolder(View view) {
            tvFromForHide = (TextView) view.findViewById(R.id.pa_from_tv);
            tvSender = (TextView) view.findViewById(R.id.pa_sender_tv);
            mCardView = (CardView) view.findViewById(R.id.pli_card);
            tvRemote = (TextView) view.findViewById(R.id.pa_tv_remote);
            tvDate = (TextView) view.findViewById(R.id.pa_tv_date);
            tvTime = (TextView) view.findViewById(R.id.pa_tv_time);
            tvTitle = (TextView) view.findViewById(R.id.pa_tv_title);
            tvDetails = (TextView) view.findViewById(R.id.pa_tv_details);
            ivPicture = (ImageView) view.findViewById(R.id.image_view_in_list_view);
        }
    }
}
