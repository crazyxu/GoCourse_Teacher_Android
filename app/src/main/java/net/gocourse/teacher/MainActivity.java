package net.gocourse.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.gocourse.function.comment.CommentFragment;
import net.gocourse.function.course.CourseFragment;
import net.gocourse.function.quiz.QuizFragment;
import net.gocourse.function.signIn.SignInFragment;
import net.gocourse.function.user.UserActivity;
import net.gocourse.model.BaseJsonModel;
import net.gocourse.model.TeacherInfo;
import net.gocourse.net.MyStringRequest;
import net.gocourse.util.ActivityUtil;
import net.gocourse.util.CodecModelUtil;
import net.gocourse.util.Constant;
import net.gocourse.util.ImageUtil;
import net.gocourse.util.SpFieldsUtil;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "NavigationViewActivity";
    private DrawerLayout drawer;
    //侧滑菜单视图
    private NavigationView navigationView;
    private Toolbar toolbar;
    private RelativeLayout user_layout;

    public boolean isFirstOpenDrawer() {
        return isFirstOpenDrawer;
    }

    public void setIsFirstOpenDrawer(boolean isFirstOpenDrawer) {
        this.isFirstOpenDrawer = isFirstOpenDrawer;
    }

    //判断是否初试打开drawer
    private boolean isFirstOpenDrawer=true;


    //请求列队
    RequestQueue queue;

    MyApplication app;

    //用户名
    TextView tvUserName;
    //简介
    TextView tvDecs;
    //头像
    ImageView ivUserHead;
    //
    private LinearLayout toolBarLayout;

    //操作字段存取
    SpFieldsUtil spu;

    public final static int SETTING_CODE=0;

    public Map<String, Object> getFragmentMap() {
        return fragmentMap;
    }

    //但从detail跳转到fragment时，以此为中介传值
    private Map<String,Object> fragmentMap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app=(MyApplication)getApplication();
        if (app.getAppTheme()==1) {
            setTheme(R.style.AppTheme_Base_dark);
        }else{
            setTheme(R.style.AppTheme_Base_Light);
        }
        setContentView(R.layout.activity_main);
        initToolBar();
        initDrawer();
        initNav();
        initView();
        initUserData();
        toFragment();

    }

    /**
     * 根据操作类型id，自动切换不同Fragment
     * @param operateId
     */
    void selectFragment(int operateId){
        navigationView.getMenu().getItem(operateId).setChecked(true);
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        switch (operateId){
            case Constant.OPERATE_COURSE:
                ft.replace(R.id.fragment_layout, new CourseFragment());
                toolbar.setTitle(R.string.nav_table);
                break;
            case Constant.OPERATE_SIGN_IN:
                ft.replace(R.id.fragment_layout, new SignInFragment());
                toolbar.setTitle(R.string.nav_signs);
                break;
            case Constant.OPERATE_EXAM:
                ft.replace(R.id.fragment_layout, new QuizFragment());
                toolbar.setTitle(R.string.nav_tests);
                break;
            case Constant.OPERATE_COMMENT:
                ft.replace(R.id.fragment_layout, new CommentFragment());
                toolbar.setTitle(R.string.nav_comment);
                break;
        }
        ft.commit();
    }

    void initView(){
        tvUserName=(TextView) super.findViewById(R.id.tv_username);
        tvDecs=(TextView)super.findViewById(R.id.tv_desc);
        ivUserHead=(ImageView)super.findViewById(R.id.civ_user_head);
        toolBarLayout=(LinearLayout)super.findViewById(R.id.toolbar_layout);
    }


    void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("我的课表");
        //设置字体颜色
        //toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);

    }
    //初始化DrawerLayout
    void initDrawer() {
        drawer = (DrawerLayout)super.findViewById(R.id.drawer);
        //切换箭头
        ActionBarDrawerToggle drawerToggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open_drawer,R.string.close_drawer){
            //drawer监听事件
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        //Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
        drawer.setDrawerListener(drawerToggle);


    }

    //初始化侧滑导航
    void initNav(){
        //侧滑菜单中顶部用户部分
        user_layout = (RelativeLayout) findViewById(R.id.user_layout);
        //监听
        user_layout.setOnClickListener(this);
        navigationView = (NavigationView) findViewById(R.id.id_nv_menu);
        //监听
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if ((menuItem.getItemId() != R.id.nav_theme) && (menuItem.getItemId() != R.id.nav_setting)) {
                            for (int i = 0; i < 4; i++) {
                                navigationView.getMenu().getItem(i).setChecked(false);
                            }
                            menuItem.setChecked(true);
                        }
                        //不同item对应不同fragment切换
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        Fragment f = null;
                        switch (menuItem.getItemId()) {
                            case R.id.nav_course:
                                f = new CourseFragment();
                                toolbar.setTitle(R.string.nav_table);
                                break;
                            case R.id.nav_signs:
                                f = new SignInFragment();
                                toolbar.setTitle(R.string.nav_signs);
                                break;
                            case R.id.nav_tests:
                                f = new QuizFragment();
                                toolbar.setTitle(R.string.nav_tests);
                                break;
                            case R.id.nav_comment:
                                f = new CommentFragment();
                                toolbar.setTitle(R.string.nav_comment);
                                break;
                            case R.id.nav_theme://切换主题
                                changeTheme();
                                //关闭Drawer
                                drawer.closeDrawers();
                                return true;
                            case R.id.nav_setting://设置
                                ActivityUtil.goNextActivityForResult(MainActivity.this, SettingActivity.class, null, SETTING_CODE);
                                //关闭Drawer
                                drawer.closeDrawers();
                                return true;

                        }
                        ft.replace(R.id.fragment_layout, f);
                        ft.commit();
                        //关闭Drawer
                        drawer.closeDrawers();

                        return true;
                    }
                });
    }

    void changeTheme(){
        //取消选择状态
        for (int i = 0; i < 4; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
        int theme = app.getAppTheme();
        theme=(0==theme?1:0);
        app.setAppTheme(theme);
        this.recreate();

    }

    //从本地或者网络获取用户数据
    void initUserData(){
        //实例化RequestQueue
        queue= Volley.newRequestQueue(this);
        //读取本地数据
        TeacherInfo teachInfo = null;
        CodecModelUtil modelUtil=new CodecModelUtil(MainActivity.this,
                Constant.SP_USER,teachInfo,TeacherInfo.TAG);
        teachInfo =(TeacherInfo)modelUtil.readFromSp();
        if (teachInfo!=null&&teachInfo.getUser()!=null){
            //设置用户名
            tvUserName.setText(teachInfo.getUser().getName());
            //设置个人简介
            tvDecs.setText(teachInfo.getUser().getDescription());
            //设置头像
            if(!ImageUtil.setIvByLocal(ivUserHead,"userHead")){
                //如果图片丢失（被第三方管理应用清除）
                String userHeadUrl=teachInfo.getUser().getAvatar();
                //从服务器重新获取图片
                ImageUtil.setIvByUrl(ivUserHead,userHeadUrl,queue,Constant.LOCAL_NAME_USER_HEAD);
            }
        }else{
            //从服务器获取个人信息
            getUserData();
        }
    }

    //获取用户信息
    private void getUserData(){
        Map<String,String> header=new HashMap<String,String>();
        header.put("token", app.getDataToken().getToken());
        String url=Constant.getUrlStr(Constant.URL_USER_INFO);
        StringRequest userDataRequest= MyStringRequest.getStringRequest(
                Constant.HTTP_METHOD_POST, url, null, header,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        BaseJsonModel jsonObj = new BaseJsonModel(response);
                        if(jsonObj.isStatus()){
                            //存储用户全部信息
                            TeacherInfo teachInfo = new TeacherInfo(jsonObj.getData());
                            CodecModelUtil modelUtil=new CodecModelUtil(MainActivity.this,
                                    Constant.SP_USER,teachInfo,TeacherInfo.TAG);
                            modelUtil.saveToSp();
                            //设置用户名等基本信息
                            String userHead,userName,userDecs;
                            userName=teachInfo.getUser().getName();
                            userHead=teachInfo.getUser().getAvatar();
                            userDecs=teachInfo.getUser().getDescription();
                            tvUserName.setText(userName);
                            tvDecs.setText(userDecs);
                            //从服务器获取头像，并缓存到内存和本地存储
                            ImageUtil.setIvByUrl(ivUserHead, userHead, queue, Constant.LOCAL_NAME_USER_HEAD);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                });
        //加入请求列队
        queue.add(userDataRequest);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //个人中心
            case R.id.user_layout:
                ActivityUtil.goNextActivity(MainActivity.this, UserActivity.class,null, false);
                drawer.closeDrawers();
                break;
        }
    }

    /**
     * 当从CourseDetailActivity跳转到指定Fragment时，在此处理相关问题
     */
    private void toFragment(){
        try{
            Bundle bundle=getIntent().getExtras();
            if (bundle!=null){
                int operateId=bundle.getInt("operateId");
                fragmentMap=new HashMap<String,Object>();
                fragmentMap.put("operateId",operateId);
                fragmentMap.put("classId",bundle.getInt("classId"));
                fragmentMap.put("courseId",bundle.getInt("courseId"));
                fragmentMap.put("courseTableId",bundle.getInt("courseTableId"));
                selectFragment(operateId);
            }else{
                selectFragment(Constant.OPERATE_COURSE);
            }
        }catch (Exception e){
            Log.e("fragmentMap", e.getMessage());
        }
    }

    public LinearLayout getToolBarLayout() {
        return toolBarLayout;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //当退出app或注销账号时，结束MainActivity
        if (requestCode==SETTING_CODE&&resultCode==SettingActivity.ACTIVITY_CODE_FINISH){
            Log.i("onActivityResult","ACTIVITY_CODE_FINISH");
            this.finish();
        }

    }

}
