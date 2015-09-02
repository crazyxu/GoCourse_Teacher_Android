package net.gocourse.function.comment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.gocourse.model.BaseJsonModel;
import net.gocourse.model.DataReviewList;
import net.gocourse.model.TbReviewReply;
import net.gocourse.net.MyStringRequest;
import net.gocourse.teacher.MainActivity;
import net.gocourse.teacher.MyApplication;
import net.gocourse.teacher.R;
import net.gocourse.ui.ListDialog;
import net.gocourse.util.CommonUtil;
import net.gocourse.util.Constant;
import net.gocourse.util.UIUtil;

import java.util.HashMap;
import java.util.Map;

public class CommentFragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private View rootView;
    private MyApplication app;
    private RequestQueue queue;
    private Context context;
    //选择查看评论的课程
    private Button btnSelectCourse;
    private String[] arrCourseTableName;
    //评论列表
    private RecyclerView rvComment;
    private MyCommentRvAdapter rvAdapter;
    //ToolBar视图
    private LinearLayout toolbarLayout;

    //数据
    //courseTableId-课程名称+班级信息
    private Map<String,String> courseTableMap;
    private DataReviewList reviewList;

    private int courseTableId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_comment, container, false);
        app=(MyApplication)getActivity().getApplication();
        context=getActivity();
        queue= Volley.newRequestQueue(getActivity());
        initView();
        getCourseTableMap();
        initCourseTable();
        return rootView;
    }

    void getCourseTableMap(){
        new AsyncTask<String,String,String>(){
            @Override
            protected String doInBackground(String... params) {
                courseTableMap=CommonUtil.getCourseTableMap(app.getDataCourseTableSearch().getList());
                return null;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                btnSelectCourse.setEnabled(true);
                //获取逆转Map（键唯一），便于设置ArrayAdapter
                Map<String,String> inverseMap= CommonUtil.getInverseMap(courseTableMap);
                arrCourseTableName=new String[inverseMap.keySet().size()];
                arrCourseTableName=inverseMap.keySet().toArray(arrCourseTableName);
            }
        }.execute();


    }

    void initView(){
        swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.comment_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshCommentRv();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        toolbarLayout=((MainActivity)getActivity()).getToolBarLayout();
        toolbarLayout.removeAllViews();
        btnSelectCourse=new Button(context);
        toolbarLayout.addView(btnSelectCourse);
        btnSelectCourse.setEnabled(false);
        btnSelectCourse.setText("选择某课程的评价");
        btnSelectCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseSelectDialog();
            }
        });
        rvComment=(RecyclerView)rootView.findViewById(R.id.rv_comment);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvComment.setLayoutManager(llm);
        reviewList = new DataReviewList();
        rvAdapter=new MyCommentRvAdapter(reviewList, context, new MyCommentRvAdapter.RvCommentCallBack() {
            @Override
            public void onClick(View v, int position , MyCommentRvAdapter.ViewHolder holder) {
                View layout=LayoutInflater.from(context).inflate(R.layout.rv_item_comment,null);
                int reviewId = reviewList.getList().get(position).getReview().getReviewID();
                switch (v.getId()){
                    case R.id.iv_reply:
                        if (reviewList.isAllowReply()){
                            String content = holder.etReplyContent.getText().toString().trim();
                            reply(content,reviewId,position);
                        }else{
                            UIUtil.showSnackBar(rootView,"不允许评论！",true);
                        }
                        break;
                    case R.id.iv_up:
                        commentLike(reviewId,true,position);
                        break;
                    case R.id.iv_down:
                        commentLike(reviewId,false,position);
                        break;
                }
            }
        },queue);
        rvComment.setAdapter(rvAdapter);
    }

    void reply(String content,int reViewId, final int position){
        String url = Constant.getUrlStr(Constant.URL_COMMENT_REPLY);
        Map<String,String> params = new HashMap<>();
        params.put("review_id",String.valueOf(reViewId));
        params.put("content", content);
        StringRequest replyRequest = MyStringRequest.getStringRequest(Constant.HTTP_METHOD_POST,
                url, params, CommonUtil.getTokenMap(app), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        BaseJsonModel jsonModel = new BaseJsonModel(s);
                        if (jsonModel.isStatus()){
                            TbReviewReply reply = TbReviewReply.parseJson(jsonModel.getData());
                            //更新数据
                            reviewList.addReply(reply,position);
                            //更新UI
                            rvAdapter.notifyDataSetChanged();
                            UIUtil.showSnackBar(rootView,"评论成功",false);
                        }
                    }
                },null);
        queue.add(replyRequest);
    }

    void commentLike(int reviewId, final boolean up, final int position){
        String url = Constant.getUrlStr(Constant.URL_COMMENT_LIKE);
        Map<String,String> params = new HashMap<>();
        params.put("review_id",String.valueOf(reviewId));
        if (up){
            params.put("type","up");
        }else{
            params.put("type","down");
        }
        StringRequest likeRequest = MyStringRequest.getStringRequest(Constant.HTTP_METHOD_POST,
                url, params, CommonUtil.getTokenMap(app), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        BaseJsonModel jsonModel = new BaseJsonModel(s);
                        if (jsonModel.isStatus()){
                            //更新数据
                            reviewList.addLike(position,up);
                            //更新UI
                            rvAdapter.notifyDataSetChanged();
                            UIUtil.showSnackBar(rootView,"评论成功",false);
                        }else{
                            UIUtil.showSnackBar(rootView,jsonModel.getMsg(),false);
                        }
                    }
                },null);
        queue.add(likeRequest);
    }


    void courseSelectDialog(){
        AlertDialog.Builder dialog= ListDialog.getDialog(getActivity(), arrCourseTableName, new ListDialog.DialogCallBack() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //更新button
                btnSelectCourse.setText(arrCourseTableName[which]);
                //更新数据和UI对应课程
                courseTableId = Integer.valueOf(CommonUtil.getKeyByValue(courseTableMap, arrCourseTableName[which]));
                refreshCommentRv();
            }
        });
        dialog.show();
    }
    //更新评论列表
    void refreshCommentRv(){
        String url=Constant.getUrlStr(Constant.URL_COMMENT_LIST);
        Map<String,String> params=new HashMap<>();
        //课程表ID
        params.put("course_table_id",String.valueOf(courseTableId));
        //需要教师恢复
        params.put("show_reply","1");
        //需要用户信息
        params.put("show_user","1");
        //需要小头像
        params.put("avatar_size","small");

        StringRequest commentRequest = MyStringRequest.getStringRequest(Constant.HTTP_METHOD_POST, url, params,
                CommonUtil.getTokenMap(app), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        BaseJsonModel jsonModel = new BaseJsonModel(s);
                        if (jsonModel.isStatus()) {
                            //解析评论信息
                            DataReviewList dataReviewList = new DataReviewList(jsonModel.getData());
                            reviewList.setValue(dataReviewList);
                            //更新UI
                            rvAdapter.notifyDataSetChanged();

                        }
                    }
                }, null);
        queue.add(commentRequest);
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
                refreshCommentRv();
            }

        }
    }



}
