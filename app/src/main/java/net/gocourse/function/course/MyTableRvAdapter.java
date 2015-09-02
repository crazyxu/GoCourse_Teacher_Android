package net.gocourse.function.course;/**
 * Created by xc on 2015/7/31.
 * GoCourse_Teacher_Android
 */

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gocourse.model.DataCourseTable;
import net.gocourse.teacher.MyApplication;
import net.gocourse.teacher.R;
import net.gocourse.util.Constant;

import java.util.List;

/**
 * User: xucan
 * Date: 2015-07-31
 * Time: 20:05
 * FIXME
 */
public class MyTableRvAdapter extends RecyclerView.Adapter<MyTableRvAdapter.ViewHolder> {
    private OnClickCallBack callBack;
    private List<DataCourseTable> data;
    private int thisDay;
    private MyApplication app;
    public MyTableRvAdapter(List<DataCourseTable> data,MyApplication app,int thisDay,OnClickCallBack callBack){
        this.data=data;
        this.callBack=callBack;
        this.app=app;
        this.thisDay=thisDay;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_course_table,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvSection.setText(Constant.ARR_SECTIONS[position]);
        holder.tvSectionTime.setText(Constant.ARR_SECTION_TIMES[position]);
        Bundle bundle = CourseDataUtil.getCourseTableData(app.getSelectWeek(),thisDay,position,data);
        if (bundle.getBoolean("has")){
            holder.position = position;
            holder.courseTableId = bundle.getInt("courseTableId");
            holder.courseId = bundle.getInt("courseId");
            holder.tvClasses.setText(bundle.getString("classes"));
            holder.tvLocation.setText(bundle.getString("location"));
            holder.tvName.setText(bundle.getString("courseName"));

        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvSection;
        public TextView tvSectionTime;
        public TextView tvName;
        public TextView tvLocation;
        public TextView tvClasses;
        //课表id
        int courseTableId=-1;
        int courseId = -1;
        int position;
        public ViewHolder(View itemView) {
            super(itemView);
            tvSection=(TextView)itemView.findViewById(R.id.tv_course_section);
            tvSectionTime=(TextView)itemView.findViewById(R.id.tv_section_time);
            tvName=(TextView)itemView.findViewById(R.id.tv_course_name);
            tvLocation=(TextView)itemView.findViewById(R.id.tv_course_location);
            tvClasses=(TextView)itemView.findViewById(R.id.tv_course_classes);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            callBack.onClick(position,courseTableId,courseId);
        }
    }
    public  interface  OnClickCallBack{
        void onClick(int position,int courseTableId,int courseId);
    }
}
