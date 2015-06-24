package chisw.com.plans.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import chisw.com.plans.R;
import chisw.com.plans.ui.activities.AlarmActivity;

/**
 * Created by Kuzlo on 23.06.2015.
 */

public class DaysOfWeekDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private final String LOG_TAG = "myLogs";
    private boolean[] mCheckedItems = { false, false, false, false, false, false, false};
    private String[] checkDaysName = { "Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat" };

    public Dialog onCreateDialog(final Bundle savedInstanceState) {
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
                        mListener.onDaysOfWeekNegativeClick(new Bundle());

                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDaysOfWeekPositiveClick(getBundleDaysOfWeek());

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
        void onDaysOfWeekPositiveClick(Bundle bundle);
        void onDaysOfWeekNegativeClick(Bundle bundle);
    }

    // Use this instance of the interface to deliver action events
    DaysOfWeekDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DaysOfWeekDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DaysOfWeekDialogListener");
        }
    }
    public Bundle getBundleDaysOfWeek()
    {
       Bundle daysOfWeek = new Bundle();

        for (int i = 0; i < 7; i++)
        {
            daysOfWeek.putBoolean(checkDaysName[i], mCheckedItems[i]);
        }

        return daysOfWeek;
    }
}
