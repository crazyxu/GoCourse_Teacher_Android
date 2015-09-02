package net.gocourse.function.course;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.gocourse.model.DataCourseTable;
import net.gocourse.teacher.MyApplication;
import net.gocourse.teacher.R;
import net.gocourse.util.ActivityUtil;

import java.util.List;

/**
 * @author xc
 * 显示某一天的课程数据,作为Viewpager内容
 */
public class TableFragment extends Fragment{
    private MyApplication app;
    private int thisDay;
    private View rootView;
    private RecyclerView rv;

    private MyTableRvAdapter adapter;

    private List<DataCourseTable> rvCourseData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_table, container, false);
        app=(MyApplication)getActivity().getApplication();
        thisDay =getArguments().getInt("thisDay");
        rv=(RecyclerView)rootView.findViewById(R.id.rv_table_fra);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (app.getDataCourseTableSearch()==null)
            return rootView;
        rvCourseData=app.getDataCourseTableSearch().getList();
        adapter=new MyTableRvAdapter(rvCourseData,app,thisDay,new MyTableRvAdapter.OnClickCallBack(){
            @Override
            public void onClick(int thisDay, int courseTableId , int courseId) {
                //表示有次课程
                if (courseTableId!=-1){
                    //跳转到CourseDetailActivity，同时传递对于课程信息DataCourseTable
                    Bundle bundle = new Bundle();
                    bundle.putInt("courseTableId", courseTableId);
                    bundle.putInt("courseId",courseId);
                    ActivityUtil.goNextActivity(getActivity(), CourseDetailActivity.class, bundle, false);
                }
            }
        });
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return rootView;
    }


    public static TableFragment newInstance(Bundle bundle){
        TableFragment fragment=new TableFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}
