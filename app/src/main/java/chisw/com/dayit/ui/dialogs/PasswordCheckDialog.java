package chisw.com.dayit.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

import chisw.com.dayit.R;

/**
 * Created by alex on 18.07.15.
 */
public class PasswordCheckDialog extends DialogFragment {

    private IPasswordCheckDialog mIPasswordCheckDialog;
    private EditText mPassET;

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        AlertDialog.Builder createProjectAlert = new AlertDialog.Builder(getActivity());
        createProjectAlert.setTitle("Enter your password");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        createProjectAlert.setView(inflater.inflate(R.layout.password_check_dialog_layout, null))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mPassET = (EditText) getDialog().findViewById(R.id.password_check_et);
                        mIPasswordCheckDialog.onDialogPositiveClick(mPassET.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mIPasswordCheckDialog.onDialogNegativeClick();
                    }
                });
        return createProjectAlert.create();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void setListener(IPasswordCheckDialog pIPasswordCheckDialog){
        mIPasswordCheckDialog = pIPasswordCheckDialog;
    }
    public interface IPasswordCheckDialog {
        void onDialogPositiveClick(String pass);
        void onDialogNegativeClick();
    }
}