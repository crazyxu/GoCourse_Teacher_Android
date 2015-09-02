package net.gocourse.function.user;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.gocourse.function.comment.MyLinearLayoutManager;
import net.gocourse.model.BaseJsonModel;
import net.gocourse.model.BaseUserInfo;
import net.gocourse.model.CollegeInfo;
import net.gocourse.model.TeacherInfo;
import net.gocourse.net.MyStringRequest;
import net.gocourse.teacher.MyApplication;
import net.gocourse.teacher.R;
import net.gocourse.ui.WebViewActivity;
import net.gocourse.util.ActivityUtil;
import net.gocourse.util.CodecModelUtil;
import net.gocourse.util.CommonUtil;
import net.gocourse.util.Constant;
import net.gocourse.util.UIUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfoFragment extends Fragment {
    private MyApplication app;
    private RequestQueue queue;
    private Context context;
    private View rootView;

    private TeacherInfo teacherInfo;

    private RecyclerView rvUserInfo;
    private RecyclerView rvCollegeInfo;
    private UserInfoRvAdapter userAdapter;
    private UserInfoRvAdapter collegeAdapter;
    private List<Map<String,String>> userList;
    private List<Map<String,String>> collegeList;

    public static UserInfoFragment newInstance(Bundle args) {
        UserInfoFragment fragment = new UserInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public UserInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_info, container, false);
        context = getActivity();
        app=(MyApplication)getActivity().getApplication();
        queue= Volley.newRequestQueue(context);
        teacherInfo=(TeacherInfo)getArguments().getSerializable("teacherInfo");
        initUserInfo();
        initCollegeInfo();
        return rootView;
    }

    void initUserInfo(){
        rvUserInfo=(RecyclerView)rootView.findViewById(R.id.user_rv_info);
        userList=new ArrayList<>();
        BaseUserInfo userInfo=teacherInfo.getUser();
        Map<String,String> mapName=new HashMap<>();
        mapName.put("title","姓名");
        mapName.put("value",userInfo.getName());
        mapName.put("isAlter","false");
        Map<String,String> mapNID=new HashMap<>();
        mapNID.put("title","教职工号");
        mapNID.put("value",userInfo.getUid());
        mapNID.put("isAlter","false");
        Map<String,String> mapEmail=new HashMap<>();
        mapEmail.put("title","邮箱");
        mapEmail.put("value",userInfo.getEmail());
        mapEmail.put("isAlter","true");
        Map<String,String> mapDes=new HashMap<>();
        mapDes.put("title","简介");
        mapDes.put("value",userInfo.getDescription());
        mapDes.put("isAlter", "true");
        userList.add(mapName);
        userList.add(mapNID);
        userList.add(mapEmail);
        userList.add(mapDes);
        userAdapter=new UserInfoRvAdapter(userList, new UserInfoRvAdapter.OnClickCallBack() {
            @Override
            public void onClick(View v,int position) {
                switch (position){
                    case 2:
                        alterEmail();
                        break;
                    case 3:
                        alterDes();
                        break;
                }
            }
        });
        rvUserInfo.setAdapter(userAdapter);
        rvUserInfo.setLayoutManager(new MyLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvUserInfo.setHasFixedSize(true);
        rvUserInfo.setFocusableInTouchMode(false);

    }

    void initCollegeInfo(){
        rvCollegeInfo=(RecyclerView)rootView.findViewById(R.id.user_rv_college);
        collegeList=new ArrayList<>();
        CollegeInfo college=teacherInfo.getCollege();
        Map<String,String> mapCollegeName=new HashMap<>();
        mapCollegeName.put("title","院系名称");
        mapCollegeName.put("value",college.getCollegeName());
        mapCollegeName.put("isAlter","false");
        Map<String,String> mapCollegeId=new HashMap<>();
        mapCollegeId.put("title","院系编号");
        mapCollegeId.put("value",college.getCollegeID()+"");
        mapCollegeId.put("isAlter","false");
        Map<String,String> mapUniName=new HashMap<>();
        mapUniName.put("title","高校名称");
        mapUniName.put("value",college.getUniName());
        mapUniName.put("isAlter","false");
        Map<String,String> mapUniId=new HashMap<>();
        mapUniId.put("title","高校编号");
        mapUniId.put("value", college.getUniID() + "");
        mapUniId.put("isAlter", "false");

        collegeList.add(mapUniName);
        collegeList.add(mapUniId);
        collegeList.add(mapCollegeName);
        collegeList.add(mapCollegeId);
        collegeAdapter=new UserInfoRvAdapter(collegeList,null);
        rvCollegeInfo.setAdapter(collegeAdapter);
        rvCollegeInfo.setLayoutManager(new MyLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rvCollegeInfo.setHasFixedSize(true);
        rvCollegeInfo.setFilterTouchesWhenObscured(false);

    }

    /**
     * 修改简介
     */
    void alterDes(){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("修改简介");
        builder.setCancelable(false);
        final View view= LayoutInflater.from(context).inflate(R.layout.dialog_user_alter_des,null);
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
                                    UIUtil.showSnackBar(rootView, "操作成功", false);
                                    //更改本地存储值，并更新UI
                                    teacherInfo.getUser().setDescription(tilDes.getEditText().getText().toString());
                                    CodecModelUtil modelUtil=new CodecModelUtil(context,
                                            Constant.SP_USER,teacherInfo,TeacherInfo.TAG);
                                    modelUtil.saveToSp();
                                    //对于序号不能错
                                    userList.get(3).put("value",tilDes.getEditText().getText().toString());
                                    userAdapter.notifyDataSetChanged();

                                } else {
                                    UIUtil.showSnackBar(view, "操作失败" + jsonModel.getMsg()
                                            , false);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                dialog.dismiss();
                                UIUtil.showSnackBar(rootView, "请求提交失败，请检查您的网络状态，稍后再试"
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

    //修改邮箱
    void alterEmail(){
        Bundle bundle= CommonUtil.getWebViewCookieBundle(app, Constant.URL_WEB_ALTER_EMAIL);
        ActivityUtil.goNextActivity(context, WebViewActivity.class, bundle, false);
    }



}
