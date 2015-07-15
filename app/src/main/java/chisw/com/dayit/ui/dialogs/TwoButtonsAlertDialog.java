package chisw.com.dayit.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import chisw.com.dayit.R;

/**
 * Created by vdbo on 14.07.15.
 */
public class TwoButtonsAlertDialog extends DialogFragment {

    private IAlertDialog mIAlertDialog;
    private String mDialogTitle;
    private String mPositiveBtnText;
    private String mNegativeBtnText;

    public interface IAlertDialog {
        void onAcceptClick();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setMessage(mDialogTitle)
                .setPositiveButton(mPositiveBtnText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mIAlertDialog.onAcceptClick();
                        dismiss();
                    }
                })
                .setNegativeButton(mNegativeBtnText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    public void setIAlertDialog(IAlertDialog pIAlertDialog) {
        mIAlertDialog = pIAlertDialog;
    }

    public void setDialogTitle(String pDialogTitle) {
        mDialogTitle = pDialogTitle;
    }

    public void setPositiveBtnText(String pPositiveBtnText) {
        mPositiveBtnText = pPositiveBtnText;
    }

    public void setNegativeBtnText(String pNegativeBtnText) {
        mNegativeBtnText = pNegativeBtnText;
    }
}
