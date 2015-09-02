package net.gocourse.function.quiz;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.gocourse.model.BaseJsonModel;
import net.gocourse.model.DataCourseMap;
import net.gocourse.model.DataCourseTable;
import net.gocourse.model.DataQuizEntity;
import net.gocourse.net.MyStringRequest;
import net.gocourse.teacher.MyApplication;
import net.gocourse.teacher.R;
import net.gocourse.ui.QuizOptionInputView;
import net.gocourse.util.CommonUtil;
import net.gocourse.util.Constant;
import net.gocourse.util.JsonUtil;
import net.gocourse.util.UIUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizAddActivity extends AppCompatActivity implements View.OnClickListener{
    //app
    private MyApplication app;
    private RequestQueue queue;
    private Toolbar toolbar;

    //课程表信息
    private List<DataCourseTable> dataCourseTableList;
    //该教师的课程Map
    Map<String,String> courseMap;
    //该教师某课程某课表ID
    Map<String,String> courseTableMap;

    //测验数据
    private List<QuizOptionInputView> optionViewList;
    private List<DataQuizEntity> quizList;


    //UI
    private TextInputLayout quizTitle;
    private TextInputLayout quizDes;
    private TextInputLayout quizIndex;
    private Button btnSubmit;

    private Spinner spCourse;
    private Spinner spCourseTable;

    private LinearLayout llOptions;

    private ImageView ivOptionAdd;
    private ImageView ivOptionRemove ;

    private RadioGroup rgCourseTable;

    private ProgressDialog loading;

    //spCourse 课程
    private String[] arrCourse;
    //spTable 课表
    private String[] arrCourseTable;

    //variable
    private int courseId=-1;
    private int courseTableId=-1;
    private String title;
    private String desc;
    private String index;
    private int quizBindTo;
    private List<String> correct;
    private List<String> options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_add);
        app=(MyApplication)getApplication();
        queue = Volley.newRequestQueue(this);
        dataCourseTableList=(app.getDataCourseTableSearch().getList());
        optionViewList=new ArrayList<>();
        correct=new ArrayList<>();
        options=new ArrayList<>();
        toolbar=(Toolbar)super.findViewById(R.id.tool_bar);
        toolbar.setTitle("添加测验");

        setSupportActionBar(toolbar);
        initView();
        getCourseMap();

    }

    void initView(){
        quizTitle=(TextInputLayout)super.findViewById(R.id.til_quiz_title);
        quizTitle.setHint("输入测验标题");
        quizDes=(TextInputLayout)super.findViewById(R.id.til_quiz_dec);
        quizDes.setHint("输入测验答案解析");
        quizIndex=(TextInputLayout)super.findViewById(R.id.til_quiz_index);
        quizIndex.setHint("输入测验索引");

        //新增，移除选择项
        llOptions=(LinearLayout)super.findViewById(R.id.ll_options);
        ivOptionAdd=(ImageView)super.findViewById(R.id.iv_add_quiz_option);
        ivOptionRemove=(ImageView)super.findViewById(R.id.iv_remove_quiz_option);
        //默认的选项个数
        int defaultOptionNum = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_quiz_option_num","4"));
        for (int i=0;i<defaultOptionNum;i++){
            QuizOptionInputView optionView = new QuizOptionInputView(QuizAddActivity.this, optionViewList.size());
            llOptions.addView(optionView);
            optionViewList.add(optionView);
        }

        //提交
        btnSubmit=(Button)super.findViewById(R.id.btn_submit);
        btnSubmit.setEnabled(false);
        ivOptionAdd.setOnClickListener(this);
        ivOptionRemove.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        //选择课程
        spCourse=(Spinner)super.findViewById(R.id.sp_add_quiz_select_course);

        spCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取点击对应courseId
                courseId = Integer.valueOf(CommonUtil.getKeyByValue(courseMap, arrCourse[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //选择课表
        spCourseTable=(Spinner)super.findViewById(R.id.sp_add_quiz_select_table);
        spCourseTable.setVisibility(View.INVISIBLE);
        spCourseTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取点击对应courseTableId
                if (courseId!=-1)
                    courseTableId = Integer.valueOf(CommonUtil.getKeyByValue(courseTableMap, arrCourseTable[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //选择添加测验对象
        rgCourseTable=(RadioGroup)super.findViewById(R.id.rg_add_table);
        RadioButton rbToTable=(RadioButton)super.findViewById(R.id.rb_to_table);
        rbToTable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    spCourseTable.setVisibility(View.VISIBLE);
                    courseTableMap = CommonUtil.getCourseTableMap(dataCourseTableList, courseId);
                    courseTableMap.put("-1", "----");
                    //获取tableAdapter数据
                    Map<String, String> inverseMap = CommonUtil.getInverseMap(courseTableMap);
                    arrCourseTable = new String[inverseMap.keySet().size()];
                    arrCourseTable = inverseMap.keySet().toArray(arrCourseTable);
                    ArrayAdapter tableAdapter = new ArrayAdapter<String>(QuizAddActivity.this,
                            android.R.layout.simple_list_item_1, arrCourseTable);
                    spCourseTable.setAdapter(tableAdapter);
                } else {
                    spCourseTable.setVisibility(View.INVISIBLE);
                }
            }
        });

        rgCourseTable.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_to_course:
                        quizBindTo=0;
                        break;
                    case R.id.rb_to_none:
                        quizBindTo=1;
                        break;
                    case R.id.rb_to_table:
                        quizBindTo=2;
                        break;
                }
            }
        });

    }

    /**
     * 获取家教师的课程Map
     */
    void getCourseMap(){
        String url= Constant.getUrlStr(Constant.URL_COURSE_LIST);
        Map<String,String> params=new HashMap<>();
        params.put("status", "-1");
        StringRequest courseListRequest= MyStringRequest.getStringRequest(Constant.HTTP_METHOD_POST,
                url, params, CommonUtil.getTokenMap(app), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        BaseJsonModel jsonModel = new BaseJsonModel(s);
                        if (jsonModel.isStatus()) {
                            courseMap = new DataCourseMap(jsonModel.getData()).getCourseMap();
                            //加载完毕后btnSelector可以点击
                            Map<String, String> inverseMap = CommonUtil.getInverseMap(courseMap);
                            arrCourse = new String[inverseMap.keySet().size()];
                            arrCourse = inverseMap.keySet().toArray(arrCourse);
                            btnSubmit.setEnabled(true);
                            ArrayAdapter courseAdapter=new ArrayAdapter<String>(QuizAddActivity.this,
                                    android.R.layout.simple_list_item_1,arrCourse);
                            spCourse.setAdapter(courseAdapter);
                        }
                    }
                }, null);
        queue.add(courseListRequest);
    }


     void addQuizSubmit(){
        boolean valid=true;
        title = quizTitle.getEditText().getText().toString();
        desc = quizDes.getEditText().getText().toString();
        index = quizIndex.getEditText().getText().toString();
        if (TextUtils.isEmpty(title)){
            quizTitle.setError("测验标题不能为空");
            quizTitle.setErrorEnabled(true);
            valid=false;
        }

        if (TextUtils.isEmpty(desc)){
            quizDes.setError("测验解析不能为空");
            quizDes.setErrorEnabled(true);
            valid=false;
        }

        if (TextUtils.isEmpty(index)){
            quizIndex.setError("测验索引不能为空");
            quizIndex.setErrorEnabled(true);
            valid=false;
        }
        for (int i=0;i<optionViewList.size();i++){
            options.add(optionViewList.get(i).getText());
            if (optionViewList.get(i).isChecked()){
                correct.add(String.valueOf(optionViewList.get(i).getIndex()+1));
            }
        }
        if (options.size()==0||correct.size()==0){
            UIUtil.showSnackBar(btnSubmit,"至少需要一个选项和答案",true);
            valid=false;
        }
         if ((courseTableId==-1)&&(quizBindTo==2)){
             valid=false;
             UIUtil.showSnackBar(btnSubmit,"请选择课表",true);
         }

        if (valid){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("检查测验信息");
            String msg="您提交的课程名为："+courseMap.get(String.valueOf(courseId))+"\n";
            if (quizBindTo==0){
                msg+="发布测验到该课程的所有班级";
            }else if (quizBindTo==1){
                msg+="该测验将不会发布到班级";
            }else{
                msg+="发布测验班级："+courseTableMap.get(String.valueOf(courseTableId));
            }
            builder.setMessage(msg);
            builder.setNegativeButton("修改信息", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setPositiveButton("确认无误", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    addQuizRequest();
                }
            });
            builder.create().show();
        }
    }

    void addQuizRequest(){

        String quizJson= JsonUtil.getJsonStr(correct, desc, index, options, title);
        //构造请求
        String url = Constant.getUrlStr(Constant.URL_QUIZ_ADD);
        Map<String,String> params=new HashMap<>();
        params.put("course_id",String.valueOf(courseId));
        params.put("quiz_json",quizJson);
        if (courseTableId!=-1){
            switch (quizBindTo){
                case 0:
                    //添加到全部课程表
                    params.put("add_my_course","1");
                    break;
                case 1:
                    //不添加到课程表
                    break;
                case 2:
                    //添加到指定课程表
                    params.put("course_table_id",String.valueOf(courseTableId));
                    break;
            }
        }
        StringRequest addQuizRequest=MyStringRequest.getStringRequest(Constant.HTTP_METHOD_POST,
                url, params, CommonUtil.getTokenMap(app), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        loading.dismiss();
                        BaseJsonModel jsonModel=new BaseJsonModel(s);
                        if (jsonModel.isStatus())
                            //添加成功
                            UIUtil.showSnackBar(btnSubmit,"添加测验成功！","继续添加",true,new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    //重新创建
                                    QuizAddActivity.this.recreate();
                                }
                            });
                        else
                            UIUtil.showSnackBar(btnSubmit,"添加测验失败！"+jsonModel.getMsg(),true);
                    }
                },null);
        queue.add(addQuizRequest);
        loading=ProgressDialog.show(this,"","正在处理...");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            QuizAddActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_add_quiz_option:
                if (optionViewList.size()>=8){
                    UIUtil.showSnackBar(btnSubmit,"选项不能超过八个",true);
                    break;
                }
                QuizOptionInputView optionView = new QuizOptionInputView(QuizAddActivity.this, optionViewList.size());
                llOptions.addView(optionView);
                optionViewList.add(optionView);
                break;
            case R.id.iv_remove_quiz_option:
                if (optionViewList.size()==1){
                    UIUtil.showSnackBar(btnSubmit,"选项不能少于一个",true);
                    break;
                }
                int nowIndex=optionViewList.size()-1;
                optionViewList.remove(nowIndex);
                llOptions.removeViewAt(nowIndex);
                break;
            case R.id.btn_submit:
                addQuizSubmit();
                break;
        }
    }

}
