package net.gocourse.function.course;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import net.gocourse.model.DataCourseTableSearch;
import net.gocourse.model.DataToken;
import net.gocourse.net.MyStringRequest;
import net.gocourse.teacher.MainActivity;
import net.gocourse.teacher.MyApplication;
import net.gocourse.teacher.R;
import net.gocourse.teacher.WelcomeActivity;
import net.gocourse.ui.ListDialog;
import net.gocourse.ui.WebViewActivity;
import net.gocourse.util.ActivityUtil;
import net.gocourse.util.CodecModelUtil;
import net.gocourse.util.CommonUtil;
import net.gocourse.util.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CourseFragment extends Fragment{
    private MyStatePagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private View rootView;
    private List<Fragment> listFra;
    private String[] arrTitle;
    private TabLayout tabLayout;

    private RequestQueue queue;

    private MyApplication app;

    private ProgressDialog loading;

    private Context context;


    //本周，从1开始
    private int thisWeek;
    //所有周数
    private String[] arrWeek=new String[25];;
    //
    private Button toolBtn;
    private AlertDialog.Builder dialog;
    //课表搜索信息
    DataCourseTableSearch dataCourseTableSearch;
    private LinearLayout toolbarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_course, container, false);
        context=getActivity();
        arrTitle=new String[]{"周一","周二","周三","周四","周五","周六","周日"};
        app=(MyApplication)getActivity().getApplication();
        initWeekSelect();
        initPager();
        initTab();
        initData();
        return rootView;
    }

    void initWeekSelect(){
        for (int i=0;i<arrWeek.length;i++){
                arrWeek[i]="第"+(i+1)+"周";
        }
        toolbarLayout=((MainActivity)getActivity()).getToolBarLayout();
        toolbarLayout.removeAllViews();
        toolBtn=new Button(context);
        toolbarLayout.addView(toolBtn);
        toolBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ListDialog.getDialog(context, arrWeek, new ListDialog.DialogCallBack() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toolBtn.setText(arrWeek[which]);
                        thisWeek = which + 1;
                        //更新UI
                        app.setSelectWeek(thisWeek);
                        pagerAdapter.notifyDataSetChanged();
                    }
                });
                dialog.show();
            }
        });

    }

    /**
     * pager
     */
    void initPager(){
        listFra=new ArrayList<Fragment>();
        for(int i=0;i<arrTitle.length;i++){
            //数据传递到Fragment
            Bundle bundle=new Bundle();
            bundle.putInt("thisDay",i);
            TableFragment tableFragment=TableFragment.newInstance(bundle);
            listFra.add(tableFragment);
        }
        //需要包含v13包
        pagerAdapter=new MyStatePagerAdapter(arrTitle,listFra,getChildFragmentManager());
        viewPager=(ViewPager)rootView.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    void initTab(){
        tabLayout = (TabLayout)rootView.findViewById(R.id.tab_layout);
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    /**
     * 获取课表数据,如果本地没有，则从网络获取
     */
    void initData(){
        new AsyncTask<String,String,String>(){
            @Override
            protected String doInBackground(String... params) {
                CodecModelUtil cmu= new CodecModelUtil(getActivity(),Constant.SP_USER,dataCourseTableSearch,DataCourseTableSearch.TAG);
                dataCourseTableSearch =(DataCourseTableSearch)cmu.readFromSp();
                return null;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //如果本地存在课表信息
                if ((dataCourseTableSearch!=null)&&(dataCourseTableSearch.getWeek()!=null)){
                    thisWeek=dataCourseTableSearch.getWeek().getWeek();
                    app.setThisWeek(thisWeek);
                    toolBtn.setText("第"+thisWeek+"周");
                    arrWeek[thisWeek-1]="当前周";
                    loading.dismiss();
                    //把数据对象应用保存在MainActivity中，以便所有Fragment都可以访问到
                    app.setDataCourseTableSearch(dataCourseTableSearch);
                    app.setSelectWeek(thisWeek);
                    pagerAdapter.notifyDataSetChanged();
                }else {
                    getCourseByNet();
                }

            }
        }.execute();
        loading=ProgressDialog.show(context, "", "正在获取您的课表信息...");
    }

    void getCourseByNet(){
        //从网络获取
        queue= Volley.newRequestQueue(getActivity());
        String url=Constant.getUrlStr(Constant.URL_COURSE_ALL);
        Map<String,String> params=new HashMap<String,String>();
        //搜索类型：teacher
        params.put("search_type","teacher");
        //班级id（可选）
        params.put("set_class_id","1");
        //班级名称（可选）
        params.put("set_class_info","1");
        //上课地点（可选）
        params.put("set_location","1");
        Map<String,String> header=new HashMap<String,String>();
        header.put("token",app.getDataToken().getToken());
        StringRequest courseRequest= MyStringRequest.getStringRequest(Constant.HTTP_METHOD_POST, url, params, header, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                BaseJsonModel jsonModel = new BaseJsonModel(s);
                if (jsonModel.isStatus()) {
                    //数据无误,更新UI
                    dataCourseTableSearch = new DataCourseTableSearch(jsonModel.getData());
                    app.setDataCourseTableSearch(dataCourseTableSearch);
                    //设置当前周
                    thisWeek = dataCourseTableSearch.getWeek().getWeek();
                    app.setThisWeek(thisWeek);
                    toolBtn.setText("第" + thisWeek + "周");
                    arrWeek[thisWeek - 1] = "当前周";
                    app.setSelectWeek(thisWeek);
                    pagerAdapter.notifyDataSetChanged();                    //保存到本地
                    CodecModelUtil cmu = new CodecModelUtil(context, Constant.SP_USER, dataCourseTableSearch, DataCourseTableSearch.TAG);
                    cmu.saveToSp();
                } else {
                    dataLoadAccountError(jsonModel.getMsg());
                }
                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                dataLoadNetError();
                loading.dismiss();
            }
        });
        //加入请求列队
        queue.add(courseRequest);
        loading=ProgressDialog.show(context, "", "正在获取您的课表信息...");
    }

    void dataLoadNetError(){

        new AlertDialog.Builder(context).setTitle("错误").setMessage("无法获取您的课表，请修复您的网络状态！")
                .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getCourseByNet();
                    }
                }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        }).setCancelable(false).show();
    }

    void dataLoadAccountError(String msg){
        //该账户无法登陆，清除token
        DataToken token=null;
        CodecModelUtil cmu=new CodecModelUtil(context, Constant.SP_USER,token,"token");
        cmu.clearSp();
        new AlertDialog.Builder(context).setTitle("错误").setMessage("无法获取您的课表，"+msg)
                .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getCourseByNet();
                    }
                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityUtil.goNextActivity(context, WelcomeActivity.class,null,true);
            }
        }).setCancelable(false).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_course, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_course_add_course:
                Bundle bundle= CommonUtil.getWebViewCookieBundle(app, Constant.URL_WEB_ADD_COURSE);
                ActivityUtil.goNextActivity(context, WebViewActivity.class,bundle,false);
                break;
            case R.id.item_course_refresh_course:
                getCourseByNet();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
