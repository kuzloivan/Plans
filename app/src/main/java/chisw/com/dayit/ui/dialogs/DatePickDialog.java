package chisw.com.dayit.ui.dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;

import java.util.Calendar;

public class DatePickDialog extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private DatePickListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        Dialog picker = new DatePickerDialog(getActivity(), this, year, month, day);
        return picker;
    }
    @Override
    public void onStart() {
        super.onStart();
        Button pButton =  ((AlertDialog) getDialog())
                .getButton(DialogInterface.BUTTON_POSITIVE);
        pButton.setText("OK");
        Button nButton =  ((AlertDialog) getDialog())
                .getButton(DialogInterface.BUTTON_NEGATIVE);
        nButton.setText("Cancel");
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
        mListener.onDatePickPositiveClick(year,month,day);
    }

    public interface DatePickListener {
        void onDatePickPositiveClick(int year, int month, int day);
    }

    public void setListener(DatePickListener pDatePickListener){
        mListener = pDatePickListener;
    }
}