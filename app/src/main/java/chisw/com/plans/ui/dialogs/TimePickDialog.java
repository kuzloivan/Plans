package chisw.com.plans.ui.dialogs;

/**
 * Created by Alex on 21.06.2015.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Formatter;

import chisw.com.plans.R;
import chisw.com.plans.utils.DataUtils;

public class TimePickDialog extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE)+1;


        Dialog picker = new TimePickerDialog(getActivity(), this, hour, minute, true);
        picker.setTitle(getResources().getString(R.string.set_time));

        return picker;
    }

    @Override
    public void onStart() {
        super.onStart();

        Button nButton =  ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
        nButton.setText(getResources().getString(R.string.set_time));

    }
    @Override
    public void onTimeSet(TimePicker view, int hours, int minute) {

        Formatter formatter = new Formatter();

        TextView tv = (TextView) getActivity().findViewById(R.id.tvTime);

        DataUtils.setCalendarHour(hours);
        DataUtils.setCalendarMinute(minute);

        tv.setText(DataUtils.fillTime());
    }

}