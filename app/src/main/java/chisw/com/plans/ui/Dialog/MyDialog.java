package chisw.com.plans.ui.Dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import chisw.com.plans.R;

/**
 * Created by Yuriy on 17.06.2015.
 */

public class MyDialog extends Dialog {
    private Context context;
    private Button button;
    private DatePicker datePicker;


    public MyDialog(Context context) {
        super(context);
        this.context = context;
    }

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_dialog);

        button = (Button) findViewById(R.id.setDateTine_btn);

    }
}