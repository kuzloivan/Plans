package chisw.com.dayit.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import chisw.com.dayit.R;

/**
 * Created by Kos on 08.07.2015.
 */
public class DeleteDialog extends DialogFragment {

    private IDelete mIDelete;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_dialog_title)
                .setPositiveButton(R.string.delete_dialog_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mIDelete.onDeleteOkClick();
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    public interface IDelete{
        void onDeleteOkClick();
    }

    public void setIDelete(IDelete pIDelete) {
        mIDelete = pIDelete;
    }
}
