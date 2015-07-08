package chisw.com.dayit.ui.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import chisw.com.dayit.R;
import chisw.com.dayit.ui.activities.AlarmActivity;

/**
 * Created by Kos on 07.07.2015.
 */
public class TaskTypeDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] s = new String[2];
        s[0] = getString(R.string.task_type_local);
        s[1] = getString(R.string.task_type_remote);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.task_type_title)
                .setItems(s, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent i = new Intent(getActivity(), AlarmActivity.class);
                                i.putExtra("isRemote", false);
                                getActivity().startActivity(i);
                                break;
                            case 1:
                                Intent ii = new Intent(getActivity(), AlarmActivity.class);
                                ii.putExtra("isRemote", true);
                                getActivity().startActivity(ii);
                                break;
                        }
                    }
                });
        return builder.create();
    }
}
