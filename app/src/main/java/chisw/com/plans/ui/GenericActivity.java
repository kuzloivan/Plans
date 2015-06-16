package chisw.com.plans.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import chisw.com.plans.R;

/**
 * Created by Alexander on 16.06.2015.
 */
public abstract class GenericActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(contentViewResId());

        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
    }

    protected abstract int contentViewResId();

    @Override
    public void setContentView(int layoutResID) {

    }
}
