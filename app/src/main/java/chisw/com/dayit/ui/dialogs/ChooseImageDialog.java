package chisw.com.dayit.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import chisw.com.dayit.R;

/**
 * Created by alex on 26.07.15.
 */
public class ChooseImageDialog extends DialogFragment {

    private ChooseImageDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] s = new String[2];
        s[0] = getString(R.string.take_picture);
        s[1] = getString(R.string.choose_from_gallery);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_image_dialog_title)
                .setItems(s, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                mListener.onTakePictureClick();
                                break;
                            case 1:
                                mListener.onChooseFromGalleryClick();
                                break;
                        }
                    }
                });
        return builder.create();
    }

    public void setListener(ChooseImageDialogListener pDaysOfWeekDialogListener){
        mListener = pDaysOfWeekDialogListener;
    }

    public interface ChooseImageDialogListener{
        void onChooseFromGalleryClick();
        void onTakePictureClick();
    }
}
