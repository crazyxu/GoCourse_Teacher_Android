package net.gocourse.function.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.gocourse.function.course.MyStatePagerAdapter;
import net.gocourse.model.BaseJsonModel;
import net.gocourse.model.TeacherInfo;
import net.gocourse.net.MyStringRequest;
import net.gocourse.teacher.MyApplication;
import net.gocourse.teacher.R;
import net.gocourse.ui.CircleImageView;
import net.gocourse.util.CodecModelUtil;
import net.gocourse.util.CommonUtil;
import net.gocourse.util.Constant;
import net.gocourse.util.ImageUtil;
import net.gocourse.util.UIUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserActivity extends AppCompatActivity{
    private static String TAG = "UserActivity";
    private Toolbar toolbar;
    private RequestQueue queue;
    private MyApplication app;
    private TeacherInfo teachInfo;
    private MyStatePagerAdapter pagerAdapter;
    private List<Fragment> listFra;
    private String[] arrTitle;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CollapsingToolbarLayout collapsingToolbar;
    private CircleImageView civHead;
    private NestedScrollView scrollView;
    private CodecModelUtil modelUtil;
    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app=(MyApplication)getApplication();
        if (app.getAppTheme()==1)
            setTheme(R.style.AppTheme_Base_dark);
        else
            setTheme(R.style.AppTheme_Base_Light);
        setContentView(R.layout.activity_user);
        //从本地获取用户信息
        modelUtil=new CodecModelUtil(UserActivity.this,
                Constant.SP_USER,teachInfo,TeacherInfo.TAG);
        teachInfo =(TeacherInfo)modelUtil.readFromSp();
        toolbar=(Toolbar)super.findViewById(R.id.toolbar);
        queue = Volley.newRequestQueue(this);
        setSupportActionBar(toolbar);
        initView();
    }

    void initView(){
        //scrollView=(NestedScrollView)super.findViewById(R.id.nsl_viewpager);
        //scrollView.setFillViewport(true);
        tabLayout=(TabLayout)super.findViewById(R.id.user_tab_layout);
        viewPager=(ViewPager)super.findViewById(R.id.user_viewpager);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.user_collapsing_toolbar);
        collapsingToolbar.setTitle(teachInfo.getUser().getName());
        initViewPager();
        initTab();
        civHead=(CircleImageView)super.findViewById(R.id.user_head);
        //设置用户头像
        if(!ImageUtil.setIvByLocal(civHead, "userHead")){
            //如果图片丢失（被第三方管理应用清除）
            String userHeadUrl=teachInfo.getUser().getAvatar();
            //从服务器重新获取图片
            ImageUtil.setIvByUrl(civHead,userHeadUrl,queue,Constant.LOCAL_NAME_USER_HEAD);
        }
    }
    void initViewPager(){
        //viewPager标题
        arrTitle=new String[]{"个人信息","课程信息"};
        //viewPager内容
        listFra=new ArrayList<>();
        Bundle bundleInfo=new Bundle();
        bundleInfo.putSerializable("teacherInfo",teachInfo);
        listFra.add(UserInfoFragment.newInstance(bundleInfo));
        listFra.add(UserCourseFragment.newInstance(null));
        pagerAdapter=new MyStatePagerAdapter(arrTitle,listFra,getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }
    void initTab(){
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_user_alter_des:
                alterDes();
                break;
            case R.id.menu_user_alter_head:
                alterHead();
                break;
            case android.R.id.home:
                UserActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 修改简介
     */
    void alterDes(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("修改简介");
        builder.setCancelable(false);
        final View view= LayoutInflater.from(this).inflate(R.layout.dialog_user_alter_des,null);
        final TextInputLayout tilDes=(TextInputLayout) view.findViewById(R.id.til_user_des);
        builder.setView(view);
        builder.setPositiveButton("确认修改", null);
        builder.setNegativeButton("取消", null);
        final AlertDialog dialog=builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String des = tilDes.getEditText().getText().toString();
                if (TextUtils.isEmpty(des)) {
                    tilDes.setErrorEnabled(true);
                    tilDes.setError("简介不能为空");
                    return;
                }
                String url = Constant.getUrlStr(Constant.URL_USER_DES_ALTER);
                Map<String, String> params = new HashMap<>();
                params.put("user_description",des);
                StringRequest request = MyStringRequest.getStringRequest(Constant.HTTP_METHOD_POST,
                        url, params, CommonUtil.getTokenMap(app), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                BaseJsonModel jsonModel = new BaseJsonModel(s);
                                if (jsonModel.isStatus()) {
                                    dialog.dismiss();
                                    UIUtil.showSnackBar(toolbar, "操作成功", false);
                                    //更改本地存储值，并更新UI
                                    teachInfo.getUser().setDescription(tilDes.getEditText().getText().toString());
                                    modelUtil.saveToSp();
                                    pagerAdapter.notifyDataSetChanged();

                                } else {
                                    UIUtil.showSnackBar(view, "操作失败" + jsonModel.getMsg()
                                            , false);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                dialog.dismiss();
                                UIUtil.showSnackBar(toolbar, "请求提交失败，请检查您的网络状态，稍后再试"
                                        , false);
                            }
                        });
                queue.add(request);
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    void alterHead(){
        new AlertDialog.Builder(this).setItems(new String[]{"本地","拍照"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    //从本地获取照片
                    case 0:
                        Intent intentFromGallery = new Intent();
                        intentFromGallery.setType("image/*"); // 设置文件类型
                        intentFromGallery
                                .setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intentFromGallery,
                                IMAGE_REQUEST_CODE);
                        break;
                    //拍照
                    case 1:
                        Intent intentFromCapture = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intentFromCapture,
                                CAMERA_REQUEST_CODE);
                        break;
                }
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode !=RESULT_CANCELED) {

            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    String fileName = String.valueOf(System.currentTimeMillis())+".jpeg";
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    //存储图片到本地
                    if (ImageUtil.saveImgOnSd(bm,fileName)){
                        String url= Environment.getExternalStorageDirectory() + Constant.LOCAL_PATH_IMAGES
                                +fileName;
                        File file=new File(url);
                        startPhotoZoom(Uri.fromFile(file));
                    }
                    break;
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        //上传图片
                        getImageToView(data);
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     */
    private Bitmap getImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            return extras.getParcelable("data");
        }
        return null;
    }
}
