package net.gocourse.teacher;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.gocourse.model.BaseJsonModel;
import net.gocourse.model.DataToken;
import net.gocourse.net.MyStringRequest;
import net.gocourse.net.TokenUtil;
import net.gocourse.ui.WebViewActivity;
import net.gocourse.util.ActivityUtil;
import net.gocourse.util.CodecModelUtil;
import net.gocourse.util.CommonUtil;
import net.gocourse.util.Constant;
import net.gocourse.util.MD5Util;
import net.gocourse.util.SpFieldsUtil;
import net.gocourse.util.UIUtil;

import java.util.HashMap;
import java.util.Map;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{
    private MyApplication app;
    private RequestQueue queue;
    private Button btnLogin;
    private Button btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app=(MyApplication)getApplication();
        if (app.getAppTheme()==1)
            setTheme(R.style.AppTheme_Base_dark);
        else
            setTheme(R.style.AppTheme_Base_Light);
        setContentView(R.layout.activity_welcome);
        initApp();
    }

    void initApp(){
        queue = Volley.newRequestQueue(this);
        DataToken token=null;
        //从本地获取DataToken
        CodecModelUtil cmu=new CodecModelUtil(this, Constant.SP_USER,token,"token");
        token=(DataToken)cmu.readFromSp();
        if(token==null){
            //如果不存在，则需要手动登陆
            initBtn();
        }else{
            //如果存在，检查token是否到期
            TokenUtil tokenUtil=new TokenUtil(queue,app);
            if(tokenUtil.isValidToken(token)){
                app.setDataToken(token);
                //休眠500ms
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ActivityUtil.goNextActivity(this, MainActivity.class, null, true);

            }else{
                //如果无效，则需重新登陆
                autoLogin();
            }
        }
    }

    private void autoLogin(){
        //从本地获取userName和password（密码为32位MD5密文）
        SpFieldsUtil spu=new SpFieldsUtil(this,Constant.SP_USER);
        //默认使用userId登录
        String userName=(String)spu.getFieldFromSp("userId",String.class.getName());
        String password=(String)spu.getFieldFromSp("passWord",String.class.getName());
        //如果没有用户名或密码为空,需要手动登陆
        if (TextUtils.isEmpty(userName)||TextUtils.isEmpty(password)){
            initBtn();
            return;
        }else{
            login(userName, password, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    //获取返回结果，进行json解析
                    BaseJsonModel jsonObj = new BaseJsonModel(s);
                    //如果请求无错误BaseJsonModel
                    if (jsonObj.isStatus()) {
                        //解析出DataToken对象
                        DataToken mdt = DataToken.parseJson(jsonObj.getData());
                        //将token保存在内存中
                        app.setDataToken(mdt);
                        //保存到本地
                        CodecModelUtil cmu = new CodecModelUtil(WelcomeActivity.this, Constant.SP_USER, mdt, "token");
                        cmu.saveToSp();
                        ActivityUtil.goNextActivity(WelcomeActivity.this, MainActivity.class, null, true);
                    } else {
                        initBtn();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    initBtn();
                }
            });
        }
    }

    private void login(String userName,String password,
           Response.Listener<String> listener,Response.ErrorListener errorListener){

        Map<String,String> params=new HashMap<String,String>();
        params.put("username",userName);
        params.put("password",password);
        params.put("pwd_type",Constant.PWD_TYPE);
        params.put("client", "android");
        String url=Constant.getUrlStr(Constant.URL_USER_LOGIN);
        //登录请求
        StringRequest loginRequest = MyStringRequest.getStringRequest(Constant.HTTP_METHOD_POST, url, params, null,
                listener,errorListener);
        queue.add(loginRequest);
    }

    void initBtn(){
        //布局文件中默认不可见，手动登陆设llBtn为可见
        LinearLayout llBtn=(LinearLayout)super.findViewById(R.id.ll_btn);
        btnLogin=(Button)super.findViewById(R.id.btn_login);
        btnReg=(Button)super.findViewById(R.id.btn_register);
        btnLogin.setOnClickListener(this);
        btnReg.setOnClickListener(this);
        llBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                loginDialog();
                break;
            case R.id.btn_register:
                UIUtil.showSnackBar(btnLogin,"教师端暂不提供注册功能",true);
                break;
        }
    }

    void loginDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = LayoutInflater.from(WelcomeActivity.this).inflate(R.layout.dialog_welcome_login,null);
        builder.setView(view);
        builder.setTitle("登录");
        builder.setCancelable(false);
        final TextInputLayout tilUser=(TextInputLayout)view.findViewById(R.id.til_login_user);
        final TextInputLayout tilPwd=(TextInputLayout)view.findViewById(R.id.til_login_pwd);
        TextView tvGetPwd=(TextView)view.findViewById(R.id.tv_forget_pwd);
        builder.setPositiveButton("登录", null);
        builder.setNegativeButton("取消", null);
        final AlertDialog dialog=builder.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = true;
                String userName = tilUser.getEditText().getText().toString().trim();
                String userPwd = tilPwd.getEditText().getText().toString().trim();
                if (TextUtils.isEmpty(userName)) {
                    valid = false;
                    tilUser.setErrorEnabled(true);
                    tilUser.setError("用户名不能为空");
                } else {
                    tilUser.setErrorEnabled(false);
                }

                if (TextUtils.isEmpty(userPwd)) {
                    valid = false;
                    tilPwd.setErrorEnabled(true);
                    tilPwd.setError("密码不能为空");
                } else {
                    tilPwd.setErrorEnabled(false);
                }
                //合法，登录
                if (valid) {
                    login(userName, MD5Util.encode(userPwd), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            //获取返回结果，进行json解析
                            BaseJsonModel jsonObj = new BaseJsonModel(s);
                            //如果请求无错误BaseJsonModel
                            if (jsonObj.isStatus()) {
                                //解析出DataToken对象
                                DataToken mdt = DataToken.parseJson(jsonObj.getData());
                                //将token保存在内存中
                                app.setDataToken(mdt);
                                //保存到本地
                                CodecModelUtil cmu = new CodecModelUtil(WelcomeActivity.this, Constant.SP_USER, mdt, "token");
                                cmu.saveToSp();
                                //MD5密码保存到本地
                                SpFieldsUtil spu= new SpFieldsUtil(WelcomeActivity.this,Constant.SP_USER);
                                spu.saveFieldToSp("passWord",String.class.getName(),MD5Util.encode(tilPwd.getEditText().toString().trim()));
                                ActivityUtil.goNextActivity(WelcomeActivity.this, MainActivity.class, null, true);
                            }else{
                                UIUtil.showSnackBar(view,"登录失败,"+jsonObj.getMsg()+jsonObj.getCode(),true);
                            }
                        }},new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse (VolleyError volleyError){
                                UIUtil.showSnackBar(btnLogin,"登录请求提交失败，请检查您的网络状态，稍后再试！",true);
                                dialog.dismiss();
                        }});
                    }
                }
            }

            );
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).

            setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       dialog.dismiss();
                                   }
                               }

            );
            tvGetPwd.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){
                //找回密码
                Bundle bundle= CommonUtil.getWebViewCookieBundle(app, Constant.URL_WEB_FORGET_PWD);
                ActivityUtil.goNextActivity(WelcomeActivity.this, WebViewActivity.class, bundle, false);
            }
            }

            );


        }

}
