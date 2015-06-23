package chisw.com.plans.ui.dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.TextView;
import java.util.Calendar;
import chisw.com.plans.R;
import chisw.com.plans.utils.DataUtils;

public class DatePickDialog extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        Dialog picker = new DatePickerDialog(getActivity(), this, year, month, day);
        picker.setTitle(getResources().getString(R.string.set_date));

        return picker;
    }
    @Override
    public void onStart() {
        super.onStart();

        Button nButton =  ((AlertDialog) getDialog())
                .getButton(DialogInterface.BUTTON_POSITIVE);
        nButton.setText(getResources().getString(R.string.set_date));

    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {

        TextView tvDate = (TextView) getActivity().findViewById(R.id.dateValue_textview);

        DataUtils.setCalendarDay(day);
        DataUtils.setCalendarMonth(month);
        DataUtils.setCalendarYear(year);

        tvDate.setText(DataUtils.getDateStrFromCalendar());
    }
}