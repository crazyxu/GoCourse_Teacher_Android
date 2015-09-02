package net.gocourse.teacher;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.gocourse.model.BaseJsonModel;
import net.gocourse.model.DataEmailStatus;
import net.gocourse.model.DataToken;
import net.gocourse.net.MyStringRequest;
import net.gocourse.ui.WebViewActivity;
import net.gocourse.util.ActivityUtil;
import net.gocourse.util.CodecModelUtil;
import net.gocourse.util.CommonUtil;
import net.gocourse.util.Constant;
import net.gocourse.util.ImageUtil;
import net.gocourse.util.SpFieldsUtil;
import net.gocourse.util.UIUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * User: xucan
 * Date: 2015-08-11
 * Time: 19:29
 * FIXME
 */
public class MyPreferenceFragment extends PreferenceFragment {
    private MyApplication app;
    private Context context;
    private RequestQueue queue;
    private Preference prefAlterEmail,prefAlterPwd,prefLogout;
    private ListPreference quizOptions;
    private String email;
    private int emailStatus;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        app=(MyApplication)getActivity().getApplication();
        context=getActivity();
        queue= Volley.newRequestQueue(getActivity());
        initPreference();
        initEmail();
        initQuizOption();
    }
    void initPreference(){
        quizOptions=(ListPreference)findPreference("pref_quiz_option_num");
        prefAlterEmail = findPreference("pref_alter_email");
        prefAlterPwd = findPreference("pref_alter_pwd");
        prefLogout = findPreference("pref_logout");
        prefAlterEmail.setOnPreferenceClickListener(new MyPreferenceClickListener());
        prefAlterPwd.setOnPreferenceClickListener(new MyPreferenceClickListener());
        prefLogout.setOnPreferenceClickListener(new MyPreferenceClickListener());
    }

    //检测邮箱状态，并修改其显示名称
    void initEmail(){
        String url = Constant.getUrlStr(Constant.URL_USER_EMAIL_STATUS);
        StringRequest emailRequest = MyStringRequest.getStringRequest(Constant.HTTP_METHOD_POST,
                url, null, CommonUtil.getTokenMap(app), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        BaseJsonModel jsonModel=new BaseJsonModel(s);
                        if (jsonModel.isStatus()){
                            DataEmailStatus dataEmailStatus=DataEmailStatus.parse(jsonModel.getData());
                            email=dataEmailStatus.getEmail();
                            emailStatus=dataEmailStatus.getStatus();
                            if (!TextUtils.isEmpty(email)){
                                prefAlterEmail.setSummary(email);
                                if (emailStatus==0)
                                    prefAlterEmail.setTitle("验证邮箱");
                                else
                                    prefAlterEmail.setTitle("修改邮箱");
                            }else{
                                prefAlterEmail.setTitle("绑定邮箱");
                            }
                        }
                    }
                }, null);
        queue.add(emailRequest);
    }

    //测验默认选项数量
    void initQuizOption(){
        quizOptions.setSummary(quizOptions.getValue());
    }

    public  class MyPreferenceClickListener implements Preference.OnPreferenceClickListener{

        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()){
                case "pref_alter_pwd":
                    alterPwd();
                    break;
                case "pref_logout":
                    logout();
                    break;
                case "pref_alter_email":
                    alterEmail();
                    break;
            }
            return false;
        }
    }
    //修改邮箱
    void alterEmail(){
        Bundle bundle= CommonUtil.getWebViewCookieBundle(app, Constant.URL_WEB_ALTER_EMAIL);
        ActivityUtil.goNextActivity(context, WebViewActivity.class, bundle, false);
    }

    //修改密码
    void alterPwd(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final View view= LayoutInflater.from(context).inflate(R.layout.dialog_setting_alter_pwd,null);
        builder.setTitle("修改密码");
        final TextInputLayout tilOldPwd=(TextInputLayout)view.findViewById(R.id.old_pwd);
        final TextInputLayout tilNewPwd=(TextInputLayout)view.findViewById(R.id.new_pwd);
        final TextInputLayout tilConfirmPwd=(TextInputLayout)view.findViewById(R.id.confirm_pwd);
        builder.setView(view);
        builder.setPositiveButton("提交", null);
        builder.setNegativeButton("取消",null);
        builder.setCancelable(false);
        final AlertDialog dialog=builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid=true;
                String oldPwd=tilOldPwd.getEditText().getText().toString();
                String newPwd=tilNewPwd.getEditText().getText().toString();
                String confirmPwd=tilConfirmPwd.getEditText().getText().toString();
                if (TextUtils.isEmpty(oldPwd)){
                    tilOldPwd.setErrorEnabled(true);
                    tilOldPwd.setError("必须输入原密码");
                    valid=false;
                }else{
                    tilOldPwd.setErrorEnabled(false);
                }

                if (TextUtils.isEmpty(newPwd)){
                    tilNewPwd.setErrorEnabled(true);
                    tilNewPwd.setError("必须输入新密码");
                    valid=false;
                }else{
                    tilNewPwd.setErrorEnabled(false);
                }

                if (TextUtils.isEmpty(confirmPwd)){
                    tilConfirmPwd.setErrorEnabled(true);
                    tilConfirmPwd.setError("必须确认新密码");
                    valid=false;
                }else{
                    tilConfirmPwd.setErrorEnabled(false);
                }

                if ((!TextUtils.isEmpty(newPwd)&&!TextUtils.isEmpty(confirmPwd))&&
                        (!newPwd.equals(confirmPwd))){
                    tilConfirmPwd.setErrorEnabled(true);
                    tilConfirmPwd.setError("确认密码不一致");
                    valid=false;
                }else{
                    tilConfirmPwd.setErrorEnabled(false);
                }
                if (valid){
                    String url=Constant.getUrlStr(Constant.URL_USER_ALTER_PWD);
                    Map<String,String> params=new HashMap<String, String>();
                    params.put("old_pwd",oldPwd);
                    params.put("new_pwd",newPwd);
                    StringRequest alterPwdRequest=MyStringRequest.getStringRequest(Constant.HTTP_METHOD_POST,
                            url, params, CommonUtil.getTokenMap(app), new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    BaseJsonModel jsonModel=new BaseJsonModel(s);
                                    if (jsonModel.isStatus()){
                                        DataToken token=DataToken.parseJson(jsonModel.getData());
                                        //将token保存在内存中
                                        app.setDataToken(token);
                                        //保存到本地
                                        CodecModelUtil cmu=new CodecModelUtil(context,Constant.SP_USER,token,"token");
                                        cmu.saveToSp();
                                        UIUtil.showSnackBar(view,"修改密码成功",false);
                                        dialog.dismiss();
                                    }else{
                                        UIUtil.showSnackBar(view,jsonModel.getMsg(),false);
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    UIUtil.showSnackBar(getView(),"提交请求错误！请检查网络状态或者稍后再试。",false);
                                    dialog.dismiss();
                                }
                            });
                    queue.add(alterPwdRequest);
                }
            }
        });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtil.showSnackBar(getView(),"取消修改密码",false);
                dialog.dismiss();
            }
        });
    }

    void logout(){
        new AlertDialog.Builder(context).setTitle("确认退出当前账号？").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = Constant.getUrlStr(Constant.URL_USER_LOGOUT);
                StringRequest logoutRequest = MyStringRequest.getStringRequest(Constant.HTTP_METHOD_POST,
                        url, getView(), null, CommonUtil.getTokenMap(app), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                BaseJsonModel jsonModel = new BaseJsonModel(s);
                                if (jsonModel.isStatus()) {
                                    //清空SharedPreference
                                    new SpFieldsUtil(context, Constant.SP_APP).clear();
                                    new SpFieldsUtil(context, Constant.SP_USER).clear();
                                    new SpFieldsUtil(context, Constant.SP_Course).clear();
                                    PreferenceManager.getDefaultSharedPreferences(context).edit().clear();
                                    //删除本地文件
                                    ImageUtil.clear();
                                    //删除Application数据
                                    app.clear();
                                    //跳转到welcome，同时结束MainActivity
                                    getActivity().setResult(SettingActivity.ACTIVITY_CODE_FINISH);
                                    ActivityUtil.goNextActivity(context, WelcomeActivity.class, null, true);
                                } else {
                                    UIUtil.showSnackBar(getView(),"操作是失败，"+jsonModel.getMsg(),true);
                                }
                            }
                        });
                queue.add(logoutRequest);
            }
        }).show();

    }

}
