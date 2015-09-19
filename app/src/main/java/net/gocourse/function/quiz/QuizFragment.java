package net.gocourse.function.quiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.gocourse.model.BaseJsonModel;
import net.gocourse.model.DataCourseMap;
import net.gocourse.model.DataCourseTable;
import net.gocourse.model.DataQuizEntity;
import net.gocourse.model.DataQuizList;
import net.gocourse.net.MyStringRequest;
import net.gocourse.teacher.MainActivity;
import net.gocourse.teacher.MyApplication;
import net.gocourse.teacher.R;
import net.gocourse.util.ActivityUtil;
import net.gocourse.util.CommonUtil;
import net.gocourse.util.Constant;
import net.gocourse.util.CourseDataUtil;
import net.gocourse.util.UIUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizFragment extends Fragment {
    //app
    private MyApplication app;
    private RequestQueue queue;
    private Context context;

    //view
    private View rootView;
    private LinearLayout toolbarLayout;
    private FloatingActionButton fabAddQuiz;
    private ProgressDialog loading;

    private RecyclerView rvQuiz;
    private Button btnSelector;

    private SwipeRefreshLayout swipeRefreshLayout;

    //adapter
    private MyQuizRvAdapter quizRvAdapter;

    //data
    //课程表信息
    private List<DataCourseTable> dataCourseTableList;
    private List<DataQuizEntity> quizList;
    //spCourse 课程
    private String[] arrCourse;
    //spTable 课表
    private String[] arrCourseTable;
    //spType 类型
    private String[] arrQuizType=new String[]{"----","已绑定的测验","未绑定测验","未绑定的共享测验"};
    private String[] arrQuizCourseType=new String[]{"----","您发布的所有测验","所有共享测验"};
    //该教师的课程Map
    Map<String,String> courseMap;
    //该教师某课程某课表ID
    Map<String,String> courseTableMap;
    //用于存储courseTableId


    //variable
    private int tableType =-1;
    private int courseType =-1;
    private int courseTableId=-1;
    private int courseId=-1;
    //0表示查询课程测验，1表示此课表已经绑定，2表示未绑定
    private int bindStatus=-1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_quiz, container, false);
        app=(MyApplication)getActivity().getApplication();
        context = getActivity();
        queue = Volley.newRequestQueue(context);
        dataCourseTableList=(app.getDataCourseTableSearch().getList());
        initView();
        getCourseMap();
        initCourseTable();
        return rootView;
    }

    void initView(){
        //CoordinatorLayout coordinatorLayout=(CoordinatorLayout)rootView.findViewById(R.id.cl_parent);
        //fab 按钮 添加测验

        fabAddQuiz = (FloatingActionButton)rootView.findViewById(R.id.fab_add_quiz);
        //fabAddQuiz.setBackgroundColor(context.getResources().getColor(R.color.common_title_bar));
        fabAddQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                ActivityUtil.goNextActivity(context,QuizAddActivity.class,null,false);
            }
        });
        //FloatingActionButton.Behavior behavior=new FloatingActionButton.Behavior();
        //测验列表
        rvQuiz = (RecyclerView)rootView.findViewById(R.id.rv_quiz);

        //behavior.layoutDependsOn(coordinatorLayout,fabAddQuiz,rvQuiz);

        quizList=new ArrayList<>();
        quizRvAdapter=new MyQuizRvAdapter(quizList,bindStatus, context,null);
        rvQuiz.setHasFixedSize(true);
        rvQuiz.setLayoutManager(new LinearLayoutManager(context));
        rvQuiz.setAdapter(quizRvAdapter);
        //测验列表类型 选择按钮
        toolbarLayout=((MainActivity)context).getToolBarLayout();
        //清除其他fragment添加的view
        toolbarLayout.removeAllViews();
        btnSelector=new Button(context);
        toolbarLayout.addView(btnSelector);
        btnSelector.setText("选择测验类型");
        btnSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizTypeSelect();
            }
        });
        //默认不可点击，只有当getCourseMap执行完毕后，才能
        btnSelector.setClickable(false);
        //刷新列表
        swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.quiz_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshRvData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    void quizBind(int quizId){
        String url="";
        if (bindStatus==1)
            //已经绑定
            url = Constant.getUrlStr(Constant.URL_QUIZ_BIND);
        else
            //未绑定
            url = Constant.getUrlStr(Constant.URL_QUIZ_UNBIND);
        Map<String,String> params = new HashMap<>();
        params.put("quiz_id",String.valueOf(quizId));
        params.put("course_table", String.valueOf(courseTableId));
        StringRequest quizBindRequest = MyStringRequest.getStringRequest(Constant.HTTP_METHOD_POST,
                url, params, CommonUtil.getTokenMap(app), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        BaseJsonModel jsonModel = new BaseJsonModel(s);
                        if (jsonModel.isStatus()){
                            //更新数据和UI
                            refreshRvData();
                            UIUtil.showSnackBar(rootView,"操作成功",false);
                        }else{
                            UIUtil.showSnackBar(rootView,"操作失败，"+jsonModel.getMsg(),false);
                        }
                    }
                },null);
        queue.add(quizBindRequest);
    }

    void quizShare(int quizId,boolean shared){
        String url="";
        if (shared)
            //已共享
            url = Constant.getUrlStr(Constant.URL_QUIZ_SHARE_CANCEL);
        else
            //未共享
            url = Constant.getUrlStr(Constant.URL_QUIZ_SHARE);
        Map<String,String> params = new HashMap<>();
        params.put("quiz_id",String.valueOf(quizId));
        StringRequest quizBindRequest = MyStringRequest.getStringRequest(Constant.HTTP_METHOD_POST,
                url, params, CommonUtil.getTokenMap(app), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        BaseJsonModel jsonModel = new BaseJsonModel(s);
                        if (jsonModel.isStatus()){
                            //更新数据和UI
                            refreshRvData();
                            UIUtil.showSnackBar(rootView,"操作成功",false);
                        }else{
                            UIUtil.showSnackBar(rootView,"操作失败，"+jsonModel.getMsg(),false);
                        }
                    }
                },null);
        queue.add(quizBindRequest);
    }

    void quizTypeSelect(){
        //builder
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setPositiveButton("确定", null);
        builder.setNegativeButton("取消", null);
        builder.setTitle("请选择测验类型");
        builder.setCancelable(false);

        //获取控件
        final View view=LayoutInflater.from(context).inflate(R.layout.dialog_quiz_type_selector, null);
        //选择课程
        Spinner spCourse = (Spinner)view.findViewById(R.id.sp_select_course);
        //选择课程测验是否包括共享
        final Spinner spCourseType=(Spinner)view.findViewById(R.id.sp_select_course_type);
        spCourseType.setAdapter(new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1,arrQuizCourseType));
        spCourseType.setVisibility(View.INVISIBLE);
        //选择课程对应课表
        final Spinner spTable = (Spinner)view.findViewById(R.id.sp_select_table);
        //默认不可见，当前选择了spCourse项并加载完数据后可见
        spTable.setVisibility(View.INVISIBLE);
        //选择对应课表的测验类型：绑定，未绑定（共享），未绑定（教师）
        final Spinner spTableType = (Spinner)view.findViewById(R.id.sp_select_quiz_type);
        //默认不可见，当前选择了spTable项后可见
        spTableType.setVisibility(View.INVISIBLE);


        ArrayAdapter courseAdapter=new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1,arrCourse);
        spCourse.setAdapter(courseAdapter);
        //默认选项
        spCourse.setSelection(0,true);
        spCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取点击对应courseId
                courseId = Integer.valueOf(CommonUtil.getKeyByValue(courseMap, arrCourse[position]));
                //根据courseId获取courseTableMap信息（courseTableId-classes）
                courseTableMap = CommonUtil.getCourseTableMap(dataCourseTableList, courseId);
                //获取tableAdapter数据
                Map<String, String> inverseMap = CommonUtil.getInverseMap(courseTableMap);

                int size = courseTableMap.keySet().size();
                String[] arrTemp = new String[size];
                arrTemp = inverseMap.keySet().toArray(arrTemp);

                arrCourseTable=new String[size+1];
                arrCourseTable[0] = "----";

                for (int i =0;i<arrTemp.length;i++){
                    arrCourseTable[i+1] = arrTemp[i];
                }
                courseTableMap.put("-1", "----");

                spTable.setVisibility(View.VISIBLE);
                spCourseType.setVisibility(View.VISIBLE);
                ArrayAdapter tableAdapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1, arrCourseTable);
                spTable.setAdapter(tableAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spCourseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取点击对应courseTableId
                courseType = position;
                //表示没有选择课表
                if (courseType==0){
                    spTable.setVisibility(View.VISIBLE);
                }else{
                    spTable.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        spTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取点击对应courseTableId
                courseTableId = Integer.valueOf(CommonUtil.getKeyByValue(courseTableMap, arrCourseTable[position]));
                //表示没有选择课表
                if (courseTableId==-1){
                    spTableType.setVisibility(View.INVISIBLE);
                    spCourseType.setVisibility(View.VISIBLE);
                    return;
                }
                spTableType.setVisibility(View.VISIBLE);
                spCourseType.setVisibility(View.INVISIBLE);
                ArrayAdapter quizTypeAdapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1, arrQuizType);
                spTableType.setAdapter(quizTypeAdapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });



        spTableType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        tableType = -1;
                        break;
                    case 1:
                        tableType = Constant.URL_QUIZ_ALL_TABLE_BIND;
                        break;
                    case 2:
                        tableType = Constant.URL_QUIZ_ALL_TABLE_UNBIND;
                        break;
                    case 3:
                        tableType = Constant.URL_QUIZ_ALL_TABLE_UNBIND_SHARED;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        builder.setView(view);

        //dialog
        final AlertDialog dialog=builder.create();
        dialog.show();
        //自定义View.OnClickListener以便于点击按钮可以不退出对话框
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果选择了table，但是没有选择quizType
                if ((courseTableId!=-1)&&(tableType ==-1)){
                    UIUtil.showSnackBar(rootView,"请选择测验类型，或者取消选择课表",false);
                    return;
                }
                if ((courseTableId==-1)&&(courseType==0)){
                    UIUtil.showSnackBar(rootView,"请选择是否包含共享测验，或选择具体课表",false);
                    return;
                }
                //记录当前选择的状态，选择课程还是课表，是否绑定，以便在Adapter中设置按钮
                if(courseTableId==-1)
                    bindStatus=0;
                else{
                    if (courseType==1)
                        bindStatus=1;
                    else
                        bindStatus=2;
                }
                refreshRvData();
                dialog.dismiss();
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                UIUtil.showSnackBar(rootView, "取消", false);
            }
        });
    }

    /**
     * 获取家教师的课程Map
     */
    void getCourseMap(){
        CourseDataUtil.getCourseMap(queue, context, app, new CourseDataUtil.CourseDataCallBack() {
            @Override
            public void onFinish(DataCourseMap dataCourseMap) {
                if (dataCourseMap != null && dataCourseMap.getCourseMap() != null) {
                    courseMap = dataCourseMap.getCourseMap();
                    //加载完毕后btnSelector可以点击
                    btnSelector.setClickable(true);
                    Map<String, String> inverseMap = CommonUtil.getInverseMap(courseMap);
                    int size = inverseMap.keySet().size();
                    String[] arrTemp = new String[size];
                    arrTemp = inverseMap.keySet().toArray(arrTemp);

                    arrCourse = new String[size + 1];
                    arrCourse[0] = "----";

                    for (int i = 0; i < arrTemp.length; i++) {
                        arrCourse[i + 1] = arrTemp[i];
                    }
                    courseMap.put("-1", "----");

                }
            }
        });



    }

    /**
     * 更新rv 数据
     */
    void refreshRvData(){
        String url="";
        Map<String,String> params=new HashMap<>();
        //如果选择课程
        if (tableType ==-1){
            //判断是否包含共享信息
            if (courseType==1)
                url=Constant.getUrlStr(Constant.URL_QUIZ_ALL_COURSE);
            else if (courseType==2)
                url=Constant.getUrlStr(Constant.URL_QUIZ_ALL_COURSE_SHARED);
            params.put("course_id", String.valueOf(courseId));
        }else{
            //选择课表
            url=Constant.getUrlStr(tableType);
            params.put("course_table", String.valueOf(courseTableId));
        }
        StringRequest quizListRequest= MyStringRequest.getStringRequest(Constant.HTTP_METHOD_POST,
                url, params, CommonUtil.getTokenMap(app), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        BaseJsonModel jsonModel=new BaseJsonModel(s);
                        if (jsonModel.isStatus()){
                            DataQuizList dataQuizList=new DataQuizList(jsonModel.getData());
                            refreshRvUI(dataQuizList.getList());
                        }
                        loading.dismiss();
                    }
                },null);
        queue.add(quizListRequest);
        loading=ProgressDialog.show(context,"","请稍等...");

    }

    /**
     * 更新rv UI
     */
    void refreshRvUI(List<DataQuizEntity> list){
        quizList.clear();
        quizList.addAll(list);
        quizRvAdapter=new MyQuizRvAdapter(quizList,bindStatus, context,new MyQuizRvAdapterCallBack());
        rvQuiz.setAdapter(quizRvAdapter);
    }


    class MyQuizRvAdapterCallBack implements MyQuizRvAdapter.ItemClickCallBack{

        @Override
        public void OnClick(int position, View v) {
            int quizId = quizList.get(position).getQuiz().getQuizID();
            boolean shred = (quizList.get(position).getQuiz().getStatus()==1);
            if(v.getId()==R.id.btn_quiz_status){
                if (bindStatus == 0)
                    //查询课程测验，只能进行共享，取消共享操作
                    quizShare(quizId,shred);
                else
                    //查询某课表测验，只能进行绑定，取消绑定操作
                    quizBind(quizId);
            }else{
                //详细信息
                if (v.getId()==R.id.quiz_rv_item){
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("quizData",quizList.get(position));
                    ActivityUtil.goNextActivity(context,QuizDetailActivity.class,bundle,false);
                }

            }
        }
    }

    //初始化相关数据
    void initCourseTable(){
        //判断是否从CourseDetailActivity跳转过来，如果是，则携带了courseTableId等数据
        Map<String,Object> map=((MainActivity)context).getFragmentMap();
        if (map!=null){
            if ((int)map.get("operateId")!=Constant.OPERATE_COURSE){
                courseTableId=(int)map.get("courseTableId");
                if (courseTableId==0)
                    return;
                courseId=(int)map.get("courseId");
                //默认显示课程数据
                courseType=1;
                bindStatus=0;
                refreshRvData();
            }

        }
    }


}
