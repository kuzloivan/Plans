package chisw.com.plans.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Alexander on 16.06.2015.
 */
public abstract class BaseActivity extends AppCompatActivity {


    protected void showToast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
