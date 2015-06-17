package chisw.com.plans.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import chisw.com.plans.R;
import chisw.com.plans.model.Plan;

/**
 * Created by Alexander on 16.06.2015.
 */
public class PlannerArrayAdapter extends ArrayAdapter<Plan> {

    private LayoutInflater inflater;

    public PlannerArrayAdapter(Context context, List<Plan> values) {
        super(context, R.layout.planner_list_view_item, values);

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        Plan currentPlan = getItem(position);

        if(convertView == null) {

            convertView = inflater.inflate(R.layout.planner_list_view_item, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.tvDate = (TextView)convertView.findViewById(R.id.pa_tv_date);
            viewHolder.tvTime = (TextView)convertView.findViewById(R.id.pa_tv_time);
            viewHolder.tvTitle = (TextView)convertView.findViewById(R.id.pa_tv_title);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.tvTitle.setText(currentPlan.getTitle());
        viewHolder.tvTime.setText("Date");
        viewHolder.tvDate.setText("12:32");

        return convertView;
    }

    private static class ViewHolder{
        public TextView tvTitle;
        public TextView tvTime;
        public TextView tvDate;
    }

}
