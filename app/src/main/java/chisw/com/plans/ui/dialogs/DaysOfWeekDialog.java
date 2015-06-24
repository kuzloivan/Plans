package chisw.com.plans.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import chisw.com.plans.R;

/**
 * Created by Kuzlo on 23.06.2015.
 */

public class DaysOfWeekDialog extends DialogFragment implements DialogInterface.OnClickListener {

    final String LOG_TAG = "myLogs";
    final boolean[] mCheckedItems = { false, false, false, false, false, false, false};
    final String[] checkDaysName = { "Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat" };

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("Choose days of week").setPositiveButton(R.string.bt_ok, this)
                .setCancelable(false)
                .setMultiChoiceItems(checkDaysName, mCheckedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which, boolean isChecked) {
                                mCheckedItems[which] = isChecked;
                            }
                        });
        return adb.create();
    }

    public void onClick(DialogInterface dialog, int which) {
        int i = 0;
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                i = R.string.bt_ok;
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
}
