package net.gocourse.function.comment;/**
 * Created by xc on 2015/7/31.
 * GoCourse_Teacher_Android
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gocourse.model.DataReviewList;
import net.gocourse.teacher.R;

/**
 * User: xucan
 * Date: 2015-07-31
 * Time: 11:51
 * FIXME
 */
public class MyReplyRvAdapter extends RecyclerView.Adapter<MyReplyRvAdapter.ViewHolder> {
    private DataReviewList reviewList;
    private int parentPosition;
    public MyReplyRvAdapter(DataReviewList reviewList,int parentPosition){
        this.reviewList=reviewList;
        this.parentPosition=parentPosition;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_comment_reply,parent,false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int userId = reviewList.getList().get(parentPosition).getReplies().get(position).getUserID();
        String content = reviewList.getList().get(parentPosition).getReplies().get(position).getContent();
        //String userName=reviewList.getUser().get(userId).getUserName();
        holder.tvReply.setText(content);
    }

    @Override
    public int getItemCount() {
        return reviewList.getList().get(parentPosition).getReplies().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvReply;
        public ViewHolder(View itemView) {
            super(itemView);
            tvReply=(TextView)itemView.findViewById(R.id.tv_reply);
        }
    }
}
