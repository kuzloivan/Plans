package chisw.com.plans.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import chisw.com.plans.R;

/**
 * Created by Alexander on 16.06.2015.
 */
public class PlannerArrayAdapter extends ArrayAdapter<String[]> {

    Context context;
    List<String[]> values;
    LayoutInflater inflater;

    public PlannerArrayAdapter(Context context,List<String[]> values) {
        super(context, R.layout.planner_list_view_item, values);
        this.context = context;
        this.values = values;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        String[] currentValues = values.get(position);

        if(convertView == null) {

            convertView = inflater.inflate(R.layout.planner_list_view_item, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.itemTvCountry = (TextView)convertView.findViewById(R.id.pa_tv_date);
            viewHolder.itemTvId = (TextView)convertView.findViewById(R.id.pa_tv_time);
            viewHolder.itemTvName = (TextView)convertView.findViewById(R.id.pa_tv_title);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.itemTvName.setText(currentValues[0]);

        viewHolder.itemTvId.setText(currentValues[1]);

        viewHolder.itemTvCountry.setText(currentValues[2]);

        return convertView;
    }

    private static class ViewHolder{

        public TextView itemTvName;

        public TextView itemTvId;

        public TextView itemTvCountry;

    }

}
