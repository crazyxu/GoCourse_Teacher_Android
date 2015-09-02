package net.gocourse.function.signIn;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.gocourse.model.BaseJsonModel;
import net.gocourse.model.DataCourseTable;
import net.gocourse.model.DataCourseTableSearch;
import net.gocourse.model.TbSignData;
import net.gocourse.model.TbSignDataList;
import net.gocourse.net.MyStringRequest;
import net.gocourse.teacher.MainActivity;
import net.gocourse.teacher.MyApplication;
import net.gocourse.teacher.R;
import net.gocourse.ui.ListDialog;
import net.gocourse.util.ActivityUtil;
import net.gocourse.util.CommonUtil;
import net.gocourse.util.Constant;
import net.gocourse.util.UIUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;

    private View rootView;
    private MyApplication app;
    private RequestQueue queue;
    //课程名-id
    private Map<String,String> courseTableMap;
    //课程选择出发按钮
    private Button btnCourseSelect;
    //签到列表
    private RecyclerView rvSign;
    private List<TbSignData> signRvData;
    private SignInRvAdapter signAdapter;
    //课表id
    private int courseTableId=-1;

    private DataCourseTableSearch dataCourseTableSearch;
    private List<DataCourseTable> dataCourseTableList;

    //spCourseTable数据
    private String[] arrCourseTableName;

    //创建签到：名称，详细（提供默认值），有效时间
    private FloatingActionButton fabAdd;

    private Context context;

    private LinearLayout toolbarLayout;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_sign, container, false);
        app=(MyApplication)getActivity().getApplication();
        context=getActivity();
        queue= Volley.newRequestQueue(getActivity());
        dataCourseTableSearch=app.getDataCourseTableSearch();
        initView();
        initCourseTable();
        getCourseTableMap();
        getSignLvData();
        return rootView;
    }

    //获取lvSignAll数据，分页，刷新
    void getSignLvData(){
        String url=Constant.getUrlStr(Constant.URL_SIGN_ALL);
        Map<String,String> params=new HashMap<>();
        params.put("show_append","1");
        StringRequest signListRequest=MyStringRequest.getStringRequest(Constant.HTTP_METHOD_POST, url, params, CommonUtil.getTokenMap(app),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        BaseJsonModel jsonModel = new BaseJsonModel(s);
                        if (jsonModel.isStatus()){
                            TbSignDataList tbSignDataList=new TbSignDataList(jsonModel.getData());
                            signRvData.clear();
                            List<TbSignData> newSignData=tbSignDataList.getList();
                            signRvData.addAll(newSignData);
                            signAdapter.notifyDataSetChanged();
                        }

                    }
                },null);
        queue.add(signListRequest);
    }

    void initView(){
        swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.sign_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSignLvData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //选择课程，课程表，节次，按钮，来自MainActivity ToolBar
        toolbarLayout=((MainActivity)getActivity()).getToolBarLayout();
        toolbarLayout.removeAllViews();
        btnCourseSelect=new Button(context);
        //toolbar.removeViews(2,toolbar.getChildCount()-2);
        toolbarLayout.addView(btnCourseSelect);
        btnCourseSelect.setText("查看指定课表签到");
        //设置按钮点击事件，弹出课程选择框
        btnCourseSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog=ListDialog.getDialog(context, arrCourseTableName, new ListDialog.DialogCallBack() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        courseTableId=Integer.valueOf(CommonUtil.getKeyByValue(courseTableMap, arrCourseTableName[which]));
                        btnCourseSelect.setText(arrCourseTableName[which]);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        btnCourseSelect.setEnabled(false);

        //发布新的测验
        fabAdd=(FloatingActionButton)rootView.findViewById(R.id.fab_add_sign);
        btnCourseSelect.setEnabled(false);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSignDialog();
            }
        });
        //签到列表
        rvSign =(RecyclerView)rootView.findViewById(R.id.rv_sign);
        //rvSign.addItemDecoration(new MyDividerItemDecoration(context));
        rvSign.setHasFixedSize(true);
        rvSign.setLayoutManager(new LinearLayoutManager(context));

        signRvData =new ArrayList<>();
        signAdapter=new SignInRvAdapter(signRvData,context,new SignInRvAdapter.BtnCallBack(){
            @Override
            public void onClick(int position) {
                Bundle bundle=new Bundle();
                bundle.putString("courseName", signRvData.get(position).getBasicData().getName());
                bundle.putInt("signId", signRvData.get(position).getBasicData().getSignID());
                ActivityUtil.goNextActivity(context,SignInDetailActivity.class,bundle,false);
            }
        });
        rvSign.setAdapter(signAdapter);

    }

    //创建新的签到对话框
    void createSignDialog(){
        //builder
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setPositiveButton("发布", null);
        builder.setNegativeButton("取消", null);
        builder.setTitle("发布新的签到");
        builder.setCancelable(false);

        //获取控件
        final View view=LayoutInflater.from(context).inflate(R.layout.dialog_sign_in_create, null);
        final TextInputLayout tilSignName=(TextInputLayout)view.findViewById(R.id.til_sign_name);
        final TextInputLayout tilSignDetail=(TextInputLayout)view.findViewById(R.id.til_sign_detail);
        final TextInputLayout tilSignTime=(TextInputLayout)view.findViewById(R.id.til_sign_time);
        final Button btnSelectCourse=(Button)view.findViewById(R.id.btn_select_course);
        btnSelectCourse.setText("选择签到课表");
        //设置提示
        tilSignName.setHint("请输入签到名称");

        tilSignDetail.setHint("请输入签到详情");
        tilSignTime.setHint("请输入有效时间（5-30分钟）");
        btnSelectCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog=ListDialog.getDialog(context, arrCourseTableName, new ListDialog.DialogCallBack() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        courseTableId=Integer.valueOf(CommonUtil.getKeyByValue(courseTableMap, arrCourseTableName[which]));
                        btnSelectCourse.setText(arrCourseTableName[which]);
                        dialog.dismiss();
                        String signName =arrCourseTableName[which]+",第"+app.getThisWeek()+"周，"+CommonUtil.getCutWeekDay();
                        tilSignName.getEditText().setText(signName);
                    }
                });
                dialog.show();
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
                //如果成功则对此对话框
                if (createSign(tilSignName,tilSignDetail,tilSignTime))
                    dialog.dismiss();
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                UIUtil.showSnackBar(rootView, "取消发布", false);
            }
        });

    }

    //创建新的签到
    boolean createSign(TextInputLayout tilSignName,TextInputLayout tilSignDetail,TextInputLayout tilSignTime) {
        //操作是否成功
        Boolean valid = true;
        String name = tilSignName.getEditText().getText().toString().trim();
        String detail = tilSignDetail.getEditText().getText().toString().trim();
        String time = tilSignTime.getEditText().getText().toString().trim();
        //判断输入合法性
        if (TextUtils.isEmpty(name)) {
            tilSignName.setError("名称必填");
            tilSignName.setErrorEnabled(true);
            valid = false;
        }
        if (TextUtils.isEmpty(detail)) {
            tilSignDetail.setError("详细信息必填");
            tilSignDetail.setErrorEnabled(true);
            valid = false;
        }
        if (TextUtils.isEmpty(time)) {
            tilSignTime.setError("有效期必填");
            tilSignTime.setErrorEnabled(true);
            valid = false;
        } else {
            //如果不全为数字
            if (!time.matches("\\d+")) {
                tilSignTime.setError("有效期格式不合法");
                tilSignTime.setErrorEnabled(true);
                valid = false;
            } else {
                //如果有效时间太大多太小
                int timeInt = Integer.valueOf(time);
                if (timeInt > 30 || timeInt < 5) {
                    tilSignTime.setError("有效期大小不合法");
                    tilSignTime.setErrorEnabled(true);
                    valid = false;
                }
            }
        }
        //满足上述条件
        if (valid) {
            //发送创建签到请求
            String url = Constant.getUrlStr(Constant.URL_SIGN_CREATE);
            Map<String, String> params = new HashMap<String, String>();
            params.put("course_table_id", String.valueOf(courseTableId));
            params.put("name", name);
            params.put("detail", detail);
            params.put("time", time);
            StringRequest createSignRequest = MyStringRequest.getStringRequest(Constant.HTTP_METHOD_POST, url, params, CommonUtil.getTokenMap(app), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    BaseJsonModel jsonModel = new BaseJsonModel(s);
                    if (jsonModel.isStatus()) {
                        UIUtil.showSnackBar(rootView,"发布成功！",false);
                        //更新数据
                        getSignLvData();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            //加入请求列队
            queue.add(createSignRequest);
        }
        return valid;
    }


    //选择课程
    void getCourseTableMap(){
        new AsyncTask<String,String,String>(){
            @Override
            protected String doInBackground(String... params) {
                courseTableMap=CommonUtil.getCourseTableMap(dataCourseTableSearch.getList());
                return null;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //获取逆转Map（键唯一），便于设置ArrayAdapter
                Map<String,String> inverseMap= CommonUtil.getInverseMap(courseTableMap);
                arrCourseTableName=new String[inverseMap.keySet().size()];
                arrCourseTableName=inverseMap.keySet().toArray(arrCourseTableName);
                btnCourseSelect.setEnabled(true);
                fabAdd.setEnabled(true);
            }
        }.execute();
    }

    //切换课程后更新数据和ui
    void refreshCourse(int courseId){


    }

    //初始化相关数据
    void initCourseTable(){
        //判断是否从CourseDetailActivity跳转过来，如果是，则携带了courseTableId等数据
        Map<String,Object> map=((MainActivity)context).getFragmentMap();
        if (map!=null){
            if ((int)map.get("operateId")!=Constant.OPERATE_COURSE){
                courseTableId=(int)map.get("courseTableId");
                if (courseTableId==0)
                    courseTableId=-1;
            }

        }
    }

}
