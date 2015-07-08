package chisw.com.dayit.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import chisw.com.dayit.R;
import chisw.com.dayit.db.entity.PlansEntity;
import chisw.com.dayit.utils.BitmapUtils;
import chisw.com.dayit.utils.DataUtils;


/**
 * Created by Alexander on 17.06.2015.
 */
public class PlannerCursorAdapter extends CursorAdapter {

    private LayoutInflater layoutInflater;
    private Uri mSelectedImageURI;
    private String mSelectedImagePath;

    public PlannerCursorAdapter(Context context) {
        super(context, null, false);

        layoutInflater = LayoutInflater.from(context);
    }

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

        int targetW = viewHolder.ivPicture.getWidth();
        int targetH = viewHolder.ivPicture.getHeight();

        int titleIndex = cursor.getColumnIndex(PlansEntity.TITLE);
        int detailsIndex = cursor.getColumnIndex(PlansEntity.DETAILS);
        int imageIndex = cursor.getColumnIndex(PlansEntity.IMAGE_PATH);
        long timeStamp = cursor.getLong(cursor.getColumnIndex(PlansEntity.TIMESTAMP));
        //int phoneNumbIndex = cursor.getColumnIndex(PlansEntity.PHONE);

        viewHolder.tvTitle.setText(cursor.getString(titleIndex));
        viewHolder.tvTime.setText(DataUtils.getTimeStringFromTimeStamp(timeStamp));
        viewHolder.tvDate.setText(DataUtils.getDateStringFromTimeStamp(timeStamp));
        viewHolder.tvDetails.setText(cursor.getString(detailsIndex));
        if(viewHolder.tvDetails.getText().length()==0) {
            viewHolder.tvDetails.setVisibility(View.GONE);
        }
        else {
            viewHolder.tvDetails.setVisibility(View.VISIBLE);
        }
        mSelectedImagePath = cursor.getString(imageIndex);
        if (mSelectedImagePath != null) {
            viewHolder.tvTitle.setTextColor(Color.WHITE);
            Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(mSelectedImagePath, targetW, targetH);
            viewHolder.ivPicture.setImageBitmap(bitmap);
            mSelectedImagePath = null;
        }

/*        int isSynchronizedIndex = cursor.getColumnIndex(PlansEntity.IS_SYNCHRONIZED);
        int isDeletedIndex = cursor.getColumnIndex(PlansEntity.IS_DELETED);
        if(cursor.getInt(isSynchronizedIndex) == 0) {
            view.setBackgroundColor(Color.parseColor("#EF5350"));
        } else if(timeStamp - System.currentTimeMillis() <= 0) {
            view.setBackgroundColor(Color.parseColor("#BDBDBD"));
        }*/
    }

    private static class ViewHolder {
        public TextView tvTitle;
        public TextView tvTime;
        public TextView tvDate;
        public TextView tvDetails;
        public ImageView ivPicture;
        //public TextView tvPhone;

        public ViewHolder(View view) {
            tvDate = (TextView) view.findViewById(R.id.pa_tv_date);
            tvTime = (TextView) view.findViewById(R.id.pa_tv_time);
            tvTitle = (TextView) view.findViewById(R.id.pa_tv_title);
            tvDetails = (TextView) view.findViewById(R.id.pa_tv_details);
            ivPicture = (ImageView) view.findViewById(R.id.image_view_in_list_view);
            //tvPhone = (TextView) view.findViewById(R.id.pa_phoneNumber_tv);
        }
    }
}
