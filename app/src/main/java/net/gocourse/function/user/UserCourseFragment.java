package net.gocourse.function.user;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import net.gocourse.model.DataCourseTable;
import net.gocourse.teacher.MainActivity;
import net.gocourse.teacher.MyApplication;
import net.gocourse.teacher.R;
import net.gocourse.util.ActivityUtil;

import java.util.List;

public class UserCourseFragment extends Fragment {
    private MyApplication app;
    private RequestQueue queue;
    private Context context;
    private View rootView;

    private RecyclerView rvTable;
    private MyUserTableRvAdapter rvAdapter;
    private List<DataCourseTable> tableList;
    public static UserCourseFragment newInstance(Bundle args) {
        UserCourseFragment fragment = new UserCourseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public UserCourseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_course, container, false);
        context = getActivity();
        app=(MyApplication)getActivity().getApplication();
        queue= Volley.newRequestQueue(context);
        initRv();
        return rootView;
    }

    void initRv(){
        rvTable=(RecyclerView)rootView.findViewById(R.id.user_course_rv);
        rvTable.setLayoutManager(new LinearLayoutManager(context));
        tableList=app.getDataCourseTableSearch().getList();
        rvAdapter = new MyUserTableRvAdapter(tableList, context, new MyUserTableRvAdapter.ClickCallBack() {
            @Override
            public void onClick(View v, int position) {
                int operateId=0;
                switch (v.getId()){
                    case R.id.btn_course_detail_sign:
                        operateId=1;
                        break;
                    case R.id.btn_course_detail_quiz:
                        operateId=2;
                        break;
                    case R.id.btn_course_detail_comment:
                        operateId=3;
                        break;
                }
                Bundle bundle = new Bundle();
                //which和Constant中operate对于关系
                bundle.putInt("operateId",operateId);
                ActivityUtil.goNextActivity(context, MainActivity.class, bundle, true);
            }
        });
        rvTable.setAdapter(rvAdapter);
    }
}
