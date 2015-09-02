package net.gocourse.teacher;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


public class AboutActivity extends AppCompatActivity{
    private ActionBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initBar();
    }

    void initBar(){
        //获取bar
        bar=getSupportActionBar();
        //设置放回箭头
        bar.setDisplayHomeAsUpEnabled(true);
        //箭头可点击
        bar.setDisplayShowHomeEnabled(true);
        bar.show();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                //结束当前activity，返回
                AboutActivity.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
