package net.gocourse.function.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import net.gocourse.model.DataCourseTable;
import net.gocourse.teacher.MainActivity;
import net.gocourse.teacher.MyApplication;
import net.gocourse.teacher.R;
import net.gocourse.util.ActivityUtil;
import net.gocourse.util.CommonUtil;

import java.util.List;

public class CourseDetailActivity extends AppCompatActivity{
    private MyApplication app;
    private Toolbar toolbar;
    private RecyclerView rvTable;
    private MyDetailTableRvAdapter rvAdapter;
    private List<DataCourseTable> tableList;

    private int courseTableId;
    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app=(MyApplication)getApplication();
        if (app.getAppTheme()==1) {
            setTheme(R.style.AppTheme_Base_dark);
        }else{
            setTheme(R.style.AppTheme_Base_Light);
        }
        setContentView(R.layout.activity_course_detail);
        initView();
        initCourseTable();
    }
    void initView(){
        toolbar=(Toolbar)super.findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        rvTable=(RecyclerView)super.findViewById(R.id.rv_table);
        rvTable.setLayoutManager(new LinearLayoutManager(this));
    }

    void initCourseTable(){
        Intent intent=getIntent();
        try{
            final Bundle bundle=intent.getExtras();
            courseId=bundle.getInt("courseId");
            tableList = CommonUtil.getByCourseId(app.getDataCourseTableSearch().getList(), courseId);
            toolbar.setTitle(tableList.get(0).getCourse().getCourseName());
            setSupportActionBar(toolbar);
            rvAdapter = new MyDetailTableRvAdapter(tableList, this, new MyDetailTableRvAdapter.ClickCallBack() {
                @Override
                public void onClick(View v, int position) {
                    int operateId=0;
                    switch (v.getId()){
                        case R.id.btn_course_detail_sign:
                            operateId=1;
                            break;
                        case R.id.btn_course_detail_quiz:
                            operateId=2;
                            break;
                        case R.id.btn_course_detail_comment:
                            operateId=3;
                            break;
                    }
                    courseTableId = tableList.get(position).getCourse().getCourseTableID();
                    courseId = tableList.get(position).getCourse().getCourseID();
                    Bundle bundle = new Bundle();
                    bundle.putInt("courseTableId", courseTableId);
                    bundle.putInt("courseId",courseId);
                    //which和Constant中operate对于关系
                    bundle.putInt("operateId",operateId);
                    ActivityUtil.goNextActivity(CourseDetailActivity.this, MainActivity.class, bundle, true);
                }
            });
            rvTable.setAdapter(rvAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                //结束当前activity，返回
                CourseDetailActivity.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }



}
