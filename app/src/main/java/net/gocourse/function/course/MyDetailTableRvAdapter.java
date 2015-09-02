package net.gocourse.function.course;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.gocourse.function.comment.MyLinearLayoutManager;
import net.gocourse.model.CourseTableIdWithClass;
import net.gocourse.model.DataCourseTable;
import net.gocourse.teacher.R;
import net.gocourse.ui.FlowLayout;
import net.gocourse.ui.FlowTextView;

import java.util.List;

/**
 * User: xucan
 * Date: 2015-08-13
 * Time: 11:16
 * FIXME
 */
public class MyDetailTableRvAdapter extends RecyclerView.Adapter<MyDetailTableRvAdapter.ViewHolder>{
    private List<DataCourseTable> tableList;
    private ClickCallBack callBack;
    private Context context;
    public MyDetailTableRvAdapter(List<DataCourseTable> tableList,Context context,ClickCallBack callBack){
        this.tableList=tableList;
        this.context=context;
        this.callBack = callBack;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_course_detail_table,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.rvLocations.setLayoutManager(new MyLinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataCourseTable courseTable = tableList.get(position);
        List<CourseTableIdWithClass> classList = courseTable.getClasses_info();
        for (int i=0;i<classList.size();i++){
            TextView textView =new FlowTextView().getFlowTextView(context,i,FlowTextView.FLOW_TEXT_SMALL);
            textView.setText(classList.get(i).getClassName());
            holder.flClasses.addView(textView);
        }
        holder.rvLocations.setAdapter(new MyDetailLocationRvAdapter(courseTable.getLocations()));
        holder.position=position;
        holder.callBack=callBack;
    }

    @Override
    public int getItemCount() {
        if (tableList!=null)
            return tableList.size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public FlowLayout flClasses;
        public RecyclerView rvLocations;
        public Button btnQuiz;
        public Button btnComment;
        public Button btnSign;
        private int position;
        private ClickCallBack callBack;
        public ViewHolder(View itemView) {
            super(itemView);
            flClasses = (FlowLayout)itemView.findViewById(R.id.fl_course_detail_classes);
            rvLocations = (RecyclerView)itemView.findViewById(R.id.rv_course_detail_locations);
            btnQuiz = (Button)itemView.findViewById(R.id.btn_course_detail_quiz);
            btnComment = (Button)itemView.findViewById(R.id.btn_course_detail_comment);
            btnSign = (Button)itemView.findViewById(R.id.btn_course_detail_sign);
            btnQuiz.setOnClickListener(this);
            btnComment.setOnClickListener(this);
            btnSign.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (callBack!=null)
                callBack.onClick(v,position);
        }
    }

    public interface ClickCallBack{
        void onClick(View v,int position);
    }
}
