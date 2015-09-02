package net.gocourse.teacher;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class SettingActivity extends AppCompatActivity {
    //activity返回码，结束源activity
    public final static int ACTIVITY_CODE_FINISH=010;
    private Toolbar toolbar;
    private MyApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app=(MyApplication)getApplication();
        if (app.getAppTheme()==1)
            setTheme(R.style.AppTheme_Base_dark);
        else
            setTheme(R.style.AppTheme_Base_Light);

        setContentView(R.layout.activity_settings);
        toolbar=(Toolbar)super.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getFragmentManager().beginTransaction().replace(R.id.fra_content, new MyPreferenceFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            SettingActivity.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
