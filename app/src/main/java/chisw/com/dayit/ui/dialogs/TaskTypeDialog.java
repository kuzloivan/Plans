package chisw.com.dayit.ui.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import chisw.com.dayit.R;
import chisw.com.dayit.ui.activities.LocalTaskActivity;
import chisw.com.dayit.ui.activities.RemoteTaskActivity;

public class TaskTypeDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] s = new String[2];
        s[0] = getString(R.string.local_plan_title);
        s[1] = getString(R.string.remote_plan_title);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.plan_type_title)
                .setItems(s, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                LocalTaskActivity.start(getActivity());
                                break;
                            case 1:
                                RemoteTaskActivity.start(getActivity());
                                break;
                        }
                    }
                });
        return builder.create();
    }
}
