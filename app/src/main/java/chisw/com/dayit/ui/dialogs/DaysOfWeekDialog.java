package chisw.com.dayit.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import chisw.com.dayit.R;


/**
 * Created by Kuzlo on 23.06.2015.
 */

public class DaysOfWeekDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private final String LOG_TAG = "myLogs";
    private boolean[] mCheckedItems = { false, false, false, false, false, false, false};
    private String[] checkDaysName = { "Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat" };
    private DaysOfWeekDialogListener mListener;

    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        initializeDays();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.days_of_week_choose)
                .setCancelable(false)
                .setMultiChoiceItems(checkDaysName, mCheckedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which, boolean isChecked) {
                                mCheckedItems[which] = isChecked;
                            }
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDaysOfWeekNegativeClick(null);
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDaysOfWeekPositiveClick(getStringSunToSatDaysToAlarm());
                    }
                });
        ;

        return builder.create();
    }

    public void onClick(DialogInterface dialog, int which) {
        int i = 0;
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                i = R.string.ok;
                break;
        }
        if (i > 0)
            Log.d(LOG_TAG, "Dialog 2: " + getResources().getString(i));
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog 2: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 2: onCancel");
    }


    public interface DaysOfWeekDialogListener {
        void onDaysOfWeekPositiveClick(String pString);
        void onDaysOfWeekNegativeClick(String pString);
    }

    private String getStringSunToSatDaysToAlarm()
    {
       String daysOfWeek = "";

        for (int i = 0; i < 7; i++) {
            daysOfWeek += mCheckedItems[i] ? "1" : "0";
        }

        return daysOfWeek;
    }

    public void setListener(DaysOfWeekDialogListener pDaysOfWeekDialogListener){
        mListener = pDaysOfWeekDialogListener;
    }

    public void initializeDays(){
        for (int i = 0; i < getArguments().getString("mDaysToAlarm").length() ; i++){
            mCheckedItems[i] = (getArguments().getString("mDaysToAlarm").charAt(i) == '1');
        }
    }
}
