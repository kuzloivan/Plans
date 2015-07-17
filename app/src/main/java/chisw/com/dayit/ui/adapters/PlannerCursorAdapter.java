package chisw.com.dayit.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Request;
import com.squareup.picasso.RequestBuilder;

import java.io.File;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import chisw.com.dayit.R;
import chisw.com.dayit.core.PApplication;
import chisw.com.dayit.db.entity.PlansEntity;
import chisw.com.dayit.model.Plan;
import chisw.com.dayit.utils.BitmapUtils;
import chisw.com.dayit.utils.DataUtils;


/**
 * Created by Alexander on 17.06.2015.
 */
public class PlannerCursorAdapter extends CursorAdapter {

    private LayoutInflater layoutInflater;
    //private Uri mSelectedImageURI;
    private String mSelectedImagePath;
    private Picasso mPicasso;

    public PlannerCursorAdapter(Context context) {
        super(context, null, false);

        layoutInflater = LayoutInflater.from(context);

        mPicasso = Picasso.with(context);

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

        int titleIndex = cursor.getColumnIndex(PlansEntity.TITLE);
        int detailsIndex = cursor.getColumnIndex(PlansEntity.DETAILS);
        int imageIndex = cursor.getColumnIndex(PlansEntity.IMAGE_PATH);
        long timeStamp = cursor.getLong(cursor.getColumnIndex(PlansEntity.TIMESTAMP));
        int daysIndex = cursor.getColumnIndex(PlansEntity.DAYS_TO_ALARM);

        //int phoneNumbIndex = cursor.getColumnIndex(PlansEntity.PHONE);

        viewHolder.tvTitle.setText(cursor.getString(titleIndex));
        viewHolder.tvTime.setText(DataUtils.getTimeStringFromTimeStamp(timeStamp));

        if(cursor.getString(daysIndex).charAt(0) == '1'){
            viewHolder.tvDate.setText(DataUtils.getDaysForRepeatingFromString(cursor.getString(daysIndex)));
        }
        else {
            viewHolder.tvDate.setText(DataUtils.getDateStringFromTimeStamp(timeStamp));
        }

        viewHolder.tvDetails.setText(cursor.getString(detailsIndex));

        mSelectedImagePath = cursor.getString(imageIndex);

        int targetW = viewHolder.ivPicture.getWidth();
        int targetH = viewHolder.ivPicture.getHeight();

        try {
            Uri imageUri;
            imageUri = Uri.fromFile(new File(mSelectedImagePath));
            String imageUriString = imageUri.toString();
            mPicasso.load(imageUriString).resize(targetW, targetH).centerCrop().into(viewHolder.ivPicture);
        } catch (Exception e) {
// Don't delete
//            Random rnd = new Random();
//            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//            viewHolder.ivPicture.setBackgroundColor(color);
//            int color = Color.argb(255,63,81,181);
//            viewHolder.ivPicture.setBackgroundColor(color);
            viewHolder.ivPicture.setImageResource(R.drawable.default_example_material);
        }
        //logMemory(context);
    }

    private void logMemory(Context context) {
        Toast.makeText(context, "Total memory = " + (Runtime.getRuntime().totalMemory() / 1024), Toast.LENGTH_SHORT).show();
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
