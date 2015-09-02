package net.gocourse.function.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.gocourse.model.DataQuizEntity;
import net.gocourse.model.TbQuizOption;
import net.gocourse.teacher.MyApplication;
import net.gocourse.teacher.R;
import net.gocourse.ui.QuizOptionDisplayView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QuizDetailActivity extends AppCompatActivity {
    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvIndex;
    private TextView tvReply;
    private TextView tvDecs;
    private LinearLayout llOption;

    private MyApplication app;

    private DataQuizEntity quizEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApplication)getApplication();
        if (app.getAppTheme()==1) {
            setTheme(R.style.AppTheme_Base_dark);
        }else{
            setTheme(R.style.AppTheme_Base_Light);
        }
        setContentView(R.layout.activity_quiz_detail);
        initView();
        setData();
    }

    void initView(){
        tvTitle = (TextView)super.findViewById(R.id.quiz_detail_title);
        tvTime = (TextView)super.findViewById(R.id.quiz_detail_time);
        tvIndex = (TextView)super.findViewById(R.id.quiz_detail_index);
        tvReply = (TextView)super.findViewById(R.id.quiz_detail_reply);
        tvDecs = (TextView)super.findViewById(R.id.quiz_detail_decs);
        llOption = (LinearLayout)super.findViewById(R.id.quiz_detail_option);
    }

    void setData(){
        quizEntity =(DataQuizEntity)getIntent().getSerializableExtra("quizData");
        tvTitle.setText(quizEntity.getQuiz().getTitle());
        SimpleDateFormat sdf=new SimpleDateFormat("MM-dd HH:mm");
        String time=sdf.format(new Date(1000*quizEntity.getQuiz().getTime()));
        tvTime.setText("发布："+time);
        tvIndex.setText("章节："+quizEntity.getQuiz().getIndex());
        tvReply.setText("答复："+quizEntity.getQuiz().getReply());
        tvDecs.setText(quizEntity.getQuiz().getDesc());

        //添加选项
        List<TbQuizOption> options = quizEntity.getOptions();
        for (int i=0;i<options.size();i++){
            boolean isChecked = options.get(i).getIsCorrect()==1;
            QuizOptionDisplayView optionView =new QuizOptionDisplayView(this, i, isChecked,
                    options.get(i).getDescription());
            llOption.addView(optionView);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            QuizDetailActivity.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
