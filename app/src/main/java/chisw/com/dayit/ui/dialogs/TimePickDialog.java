package chisw.com.dayit.ui.dialogs;

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
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickDialog extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    private TimePickListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE) + 1;
        Dialog picker = new TimePickerDialog(getActivity(), this, hour, minute, true);
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
    public void onTimeSet(TimePicker view, int hours, int minute) {
        mListener.onTimePickPositiveClick(hours,minute);
    }

    public interface TimePickListener{
        void onTimePickPositiveClick(int pHours, int mMinute);
    }

    public void setListener(TimePickListener pTimePickListener){
        mListener = pTimePickListener;
    }
}