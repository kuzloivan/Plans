package chisw.com.plans.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import chisw.com.plans.R;
import chisw.com.plans.core.PApplication;
import chisw.com.plans.model.Plan;
import chisw.com.plans.utils.BitmapUtils;
import chisw.com.plans.utils.DataUtils;

/**
 * Created by Alexander on 16.06.2015.
 */
public class PlannerArrayAdapter extends ArrayAdapter<Plan> {

    private LayoutInflater inflater;
    private Uri mSelectedImageURI;
    private String mSelectedImagePath;

    public PlannerArrayAdapter(Context context, List<Plan> values) {
        super(context, R.layout.planner_list_view_item, values);

        inflater = LayoutInflater.from(context);
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        ViewHolder viewHolder;
//        Plan currentPlan = getItem(position);
//
//        if(convertView == null) {
//
//            convertView = inflater.inflate(R.layout.planner_list_view_item, parent, false);
//            viewHolder = new ViewHolder();
//
//            viewHolder.tvDate = (TextView)convertView.findViewById(R.id.pa_tv_date);
//            viewHolder.tvTime = (TextView)convertView.findViewById(R.id.pa_tv_time);
//            viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.pa_tv_title);
//            viewHolder.ivPicture = (ImageView)convertView.findViewById(R.id.image_view_pictures);
//            // todo: add DetailsView
//            convertView.setTag(viewHolder);
//        }
//        else{
//            viewHolder = (ViewHolder)convertView.getTag();
//        }
//
//        viewHolder.tvTitle.setText(currentPlan.getTitle());
//        viewHolder.tvTime.setText(DataUtils.getTimeStringFromTimeStamp(currentPlan.getTimeStamp()));
//        viewHolder.tvDate.setText(DataUtils.getDateStringFromTimeStamp(currentPlan.getTimeStamp()));
//
//        //Ne fact
//        final String[] proj = {MediaStore.Audio.Media.DATA};
//        final Cursor cursor;
//        //mSelectedImageURI = ;
//        cursor = getContext().getContentResolver().query(mSelectedImageURI, proj, null, null, null);
//        final int column_index_i = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToLast();
//        mSelectedImagePath = cursor.getString(column_index_i);
//        Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(mSelectedImagePath, 110, 110);
//        viewHolder.ivPicture.setImageResource(R.drawable.ic_alarm);
//
//
//
//        return convertView;
//    }
//
//    private static class ViewHolder{
//        public TextView tvTitle;
//        public TextView tvTime;
//        public TextView tvDate;
//        public TextView tvDetails;
//        public ImageView ivPicture;
//    }

}
