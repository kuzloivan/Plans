package chisw.com.plans.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import chisw.com.plans.R;
import chisw.com.plans.db.entity.PlansEntity;
import chisw.com.plans.utils.DataUtils;

/**
 * Created by Alexander on 17.06.2015.
 */
public class PlannerCursorAdapter extends CursorAdapter {

    private LayoutInflater layoutInflater;

    public PlannerCursorAdapter(Context context) {
        super(context, null, false);

        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = layoutInflater.inflate(R.layout.planner_list_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder();

        viewHolder.tvDate = (TextView)view.findViewById(R.id.pa_tv_date);
        viewHolder.tvTime = (TextView)view.findViewById(R.id.pa_tv_time);
        viewHolder.tvTitle = (TextView)view.findViewById(R.id.pa_tv_title);

        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder)view.getTag();

        int titleIndex = cursor.getColumnIndex(PlansEntity.TITLE);
        int timeStampIndex = cursor.getColumnIndex(PlansEntity.TIMESTAMP);
        int timeStamp = cursor.getInt(timeStampIndex);

        viewHolder.tvTitle.setText(cursor.getString(titleIndex));
        viewHolder.tvTime.setText(DataUtils.getTimeStringFromTimeStamp(timeStamp));
        viewHolder.tvDate.setText(DataUtils.getDateStringFromTimeStamp(timeStamp));
    }

    private static class ViewHolder{
        public TextView tvTitle;
        public TextView tvTime;
        public TextView tvDate;
    }
}
