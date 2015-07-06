package chisw.com.plans.ui.activities;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import chisw.com.plans.R;


/**
 * Created by Alexander on 16.06.2015.
 */

public abstract class ToolbarActivity extends BaseActivity {

    private Toolbar toolbar;
    private Paint mShadow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(contentViewResId());

        initToolbar();
    }

    private void initToolbar() {
        toolbar  = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        mShadow.setColor(Color.BLUE);
//        mShadow.setStyle(Paint.Style.FILL);
//        mShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mShadow.setShadowLayer(10.0f, 0.0f, 3.5f, Color.argb(100, 0, 0, 0));
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

    protected void initBackButton(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}
