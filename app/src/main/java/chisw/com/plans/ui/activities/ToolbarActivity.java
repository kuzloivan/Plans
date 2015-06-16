package chisw.com.plans.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import chisw.com.plans.R;

/**
 * Created by Alexander on 16.06.2015.
 */
public abstract class ToolbarActivity extends BaseActivity {

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract int contentViewResId();

    protected void showToast(String text){
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setContentView(int layoutResID) {

    }
}
