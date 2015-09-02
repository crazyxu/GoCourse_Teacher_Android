package net.gocourse.function.comment;/**
 * Created by xc on 2015/7/30.
 * GoCourse_Teacher_Android
 */

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import net.gocourse.model.DataReview;
import net.gocourse.model.DataReviewList;
import net.gocourse.model.TbUserSimple;
import net.gocourse.teacher.R;
import net.gocourse.util.ImageUtil;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * User: xucan
 * Date: 2015-07-30
 * Time: 11:56
 * FIXME
 */
public class MyCommentRvAdapter extends RecyclerView.Adapter<MyCommentRvAdapter.ViewHolder> {
    private DataReviewList reviewList;
    private Context context;
    private RvCommentCallBack callBack;
    private RequestQueue queue;
    private Map<Integer,MyReplyRvAdapter> replyRvAdapterMap;
    public MyCommentRvAdapter(DataReviewList reviewList,Context context,RvCommentCallBack callBack,RequestQueue queue){
        this.reviewList=reviewList;
        this.context=context;
        this.callBack=callBack;
        this.queue=queue;
        replyRvAdapterMap=new HashMap<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.rv_item_comment, parent, false);
        ViewHolder holder=new ViewHolder(v);
        holder.rvReply.setLayoutManager(new MyLinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ivDown.setOnClickListener(new RvCommentListener(position,callBack,holder));
        holder.ivUp.setOnClickListener(new RvCommentListener(position,callBack,holder));
        holder.ivReply.setOnClickListener(new RvCommentListener(position,callBack,holder));
        DataReview dataReview=reviewList.getList().get(position);
        Map<String,TbUserSimple> userMap=reviewList.getUser();
        int commenterId=dataReview.getReview().getUserID();
        //评论者头像
        String headUrl=userMap.get(String.valueOf(commenterId)).getUserAvatar();
        ImageUtil.imageLoader(holder.ivCommenterHead,queue,headUrl,R.drawable.comment_default_head_72,
                R.drawable.comment_default_head_72,72,72);
        //评论者姓名
        holder.tvCommenterName.setText(userMap.get(String.valueOf(commenterId)).getUserName());
        //评论时间
        SimpleDateFormat sdf=new SimpleDateFormat("MM月dd日 HH:mm");
        String time=sdf.format(1000*dataReview.getReview().getTime());
        holder.tvCommentTime.setText(time);
        //评论内容
        holder.tvCommentContent.setText(dataReview.getReview().getContent());
        //好评次数
        holder.tvUpNum.setText(dataReview.getReview().getUpNum()+"");
        //差评次数
        holder.tvDownNum.setText(dataReview.getReview().getDownNum()+"");
        //评分
        holder.rbRating.setMax(5);
        holder.rbRating.setRating(dataReview.getReview().getRating());
        //回复列表
        MyReplyRvAdapter adapter;
        //复用Adapter
        if (replyRvAdapterMap.containsKey(position)){
            adapter=replyRvAdapterMap.get(position);
        }else{
            adapter=new MyReplyRvAdapter(reviewList,position);
            replyRvAdapterMap.put(position,adapter);
        }
        holder.rvReply.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        if (reviewList.getList()!=null)
            return reviewList.getList().size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivCommenterHead;
        public TextView tvCommenterName;
        public TextView tvCommentTime;
        public TextView tvCommentContent;
        public ImageView ivUp;
        public TextView tvUpNum;
        public ImageView ivDown;
        public TextView tvDownNum;
        public RatingBar rbRating;
        public RecyclerView rvReply;
        public EditText etReplyContent;
        public ImageView ivReply;
        public ViewHolder(View itemView) {
            super(itemView);
            ivCommenterHead=(ImageView)itemView.findViewById(R.id.iv_commenter_head);
            tvCommenterName=(TextView)itemView.findViewById(R.id.tv_commenter_name);
            tvCommentTime=(TextView)itemView.findViewById(R.id.tv_comment_time);
            tvCommentContent=(TextView)itemView.findViewById(R.id.tv_comment_content);
            ivUp=(ImageView)itemView.findViewById(R.id.iv_up);
            tvUpNum=(TextView)itemView.findViewById(R.id.tv_up_num);
            ivDown=(ImageView)itemView.findViewById(R.id.iv_down);
            tvDownNum=(TextView)itemView.findViewById(R.id.tv_down_num);
            rbRating=(RatingBar)itemView.findViewById(R.id.rb_comment_rating);
            rvReply=(RecyclerView)itemView.findViewById(R.id.rv_reply);
            etReplyContent=(EditText)itemView.findViewById(R.id.et_reply_content);
            ivReply=(ImageView)itemView.findViewById(R.id.iv_reply);

        }


    }

    /**
     * 回调接口，在CommentFragment中实现
     */
    interface RvCommentCallBack{
        void onClick(View v,int position,ViewHolder holder);
    }

    /**
     * 控件监听，调用RvCommentCallBack接口，把view和position传递到实现类中
     */
    class RvCommentListener  implements View.OnClickListener{
        private int position;
        private RvCommentCallBack callBack;
        private ViewHolder holder;
        public RvCommentListener(int position,RvCommentCallBack callBack,ViewHolder holder){
            this.position=position;
            this.callBack=callBack;
            this.holder=holder;
        }
        @Override
        public void onClick(View v) {
            callBack.onClick(v,position,holder);
        }
    }

}
