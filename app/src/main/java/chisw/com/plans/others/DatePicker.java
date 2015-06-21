package chisw.com.plans.others;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import chisw.com.plans.R;
import chisw.com.plans.ui.activities.AlarmActivity;

public class DatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private Calendar calendar = Calendar.getInstance();


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // определяем текущую дату
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // создаем DatePickerDialog и возвращаем его
        Dialog picker = new DatePickerDialog(getActivity(), this,
                year, month, day);
        picker.setTitle(getResources().getString(R.string.set_date));

        return picker;
    }
    @Override
    public void onStart() {
        super.onStart();
        // добавляем кастомный текст для кнопки
        Button nButton =  ((AlertDialog) getDialog())
                .getButton(DialogInterface.BUTTON_POSITIVE);
        nButton.setText(getResources().getString(R.string.set_date));

    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year,
                          int month, int day) {

        TextView tv = (TextView) getActivity().findViewById(R.id.tvDate);
        tv.setText("Date: " + day + "-" + month + "-" + year);

        AlarmActivity.setCalendarDay(day);
        AlarmActivity.setCalendarMonth(month);
        AlarmActivity.setCalendarYear(year);
    }
}