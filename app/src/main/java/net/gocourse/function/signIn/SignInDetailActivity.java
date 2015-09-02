package net.gocourse.function.signIn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import net.gocourse.teacher.R;

public class SignInDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_detail);
        Bundle bundle=getIntent().getExtras();
        toolbar=(Toolbar)super.findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle((String)bundle.get("courseName"));

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            SignInDetailActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
