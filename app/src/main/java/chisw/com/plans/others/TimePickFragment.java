package chisw.com.plans.others;

/**
 * Created by ��������� on 21.06.2015.
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

import chisw.com.plans.R;
import chisw.com.plans.ui.activities.AlarmActivity;

public class TimePickFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private Calendar calendar = Calendar.getInstance();;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // ������������� ������� ����� ��� TimePicker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE)+1;

        // ������� TimePickerDialog � ���������� ���
        Dialog picker = new TimePickerDialog(getActivity(), this, hour, minute, true);
        picker.setTitle(getResources().getString(R.string.set_time));

        return picker;
    }

    @Override
    public void onStart() {
        super.onStart();
        // ��������� ��������� ����� ��� ������
        Button nButton =  ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
        nButton.setText(getResources().getString(R.string.set_time));

    }
    @Override
    public void onTimeSet(TimePicker view, int hours, int minute) {
        // ������� ��������� �����
        TextView tv = (TextView) getActivity().findViewById(R.id.tvTime);
        tv.setText("Time: " + hours + ":" + minute);

        AlarmActivity.setCalendarHour(hours);
        AlarmActivity.setCalendarMinute(minute);

    }

}