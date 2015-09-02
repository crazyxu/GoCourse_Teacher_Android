package net.gocourse.function.quiz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.readystatesoftware.viewbadger.BadgeView;

import net.gocourse.model.DataQuizEntity;
import net.gocourse.model.TbQuiz;
import net.gocourse.teacher.R;
import net.gocourse.util.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * User: xucan
 * Date: 2015-07-31
 * Time: 17:01
 * FIXME
 */
public class MyQuizRvAdapter extends RecyclerView.Adapter<MyQuizRvAdapter.ViewHolder> {
    private List<DataQuizEntity> quizList;
    private ItemClickCallBack callBack;
    private Context context;
    //0表示查询课程测验，1表示此课表已经绑定，2表示未绑定
    private int bindStatus;
    public MyQuizRvAdapter(List<DataQuizEntity> quizList,int bindStatus,Context context,ItemClickCallBack callBack){
        this.quizList= quizList;
        this.callBack=callBack;
        this.context=context;
        this.bindStatus=bindStatus;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_quiz,parent,false);
        ViewHolder holder=new ViewHolder(v,callBack);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.position = position;
        TbQuiz quiz=quizList.get(position).getQuiz();
        String title=quiz.getTitle();
        holder.tvTitle.setText(title);
        holder.tvIndex.setText("章节："+quiz.getIndex());

        BadgeView badge = new BadgeView(context,holder.tvReply);
        badge.setText(quiz.getReply() + "");
        badge.setBadgeBackgroundColor(context.getResources().getColor(R.color.badge_sign_count_background_color));
        //居中
        badge.setBadgePosition(BadgeView.POSITION_CENTER);
        badge.setTextSize(20);
        badge.show();

        holder.tvType.setText("题型："+Constant.ARR_QUIZ_TYPE[Integer.valueOf(quiz.getType())]);
        SimpleDateFormat sdf=new SimpleDateFormat("MM月dd日 HH:mm");
        String time=sdf.format(new Date(1000*quiz.getTime()));
        holder.tvTime.setText("发布于："+time);
        if (quiz.getStatus()==2){
            holder.btnStatus.setText("已被删除");
        }else{
            switch (bindStatus){
                case 0:
                    if (quiz.getStatus()==1){
                        holder.btnStatus.setText("取消共享");
                    }else{
                        holder.btnStatus.setText("共享测验");
                    }
                    break;
                case 1:
                    holder.btnStatus.setText("取消绑定");
                    break;
                case 2:
                    holder.btnStatus.setText("绑定测验");
            }
        }
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private int position;
        ItemClickCallBack callBack;

        TextView tvTitle;
        TextView tvReply;
        TextView tvIndex;
        TextView tvTime;
        TextView tvType;
        Button btnStatus;

        public ViewHolder(View itemView,ItemClickCallBack callBack) {
            super(itemView);
            this.callBack=callBack;

            tvTitle=(TextView)itemView.findViewById(R.id.tv_quiz_title);
            tvReply=(TextView)itemView.findViewById(R.id.tv_quiz_reply);
            tvIndex=(TextView)itemView.findViewById(R.id.tv_quiz_index);
            tvTime=(TextView)itemView.findViewById(R.id.tv_quiz_time);
            tvType=(TextView)itemView.findViewById(R.id.tv_quiz_type);
            btnStatus=(Button)itemView.findViewById(R.id.btn_quiz_status);
            btnStatus.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (callBack!=null)
                callBack.OnClick(position,v);
        }
    }

    public interface ItemClickCallBack{
        void OnClick(int position,View v);
    }
}
