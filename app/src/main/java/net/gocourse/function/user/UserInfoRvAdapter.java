package net.gocourse.function.user;/**
 * Created by xc on 2015/7/31.
 * GoCourse_Teacher_Android
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.gocourse.teacher.R;

import java.util.List;
import java.util.Map;

/**
 * User: xucan
 * Date: 2015-07-31
 * Time: 20:05
 * FIXME
 */
public class UserInfoRvAdapter extends RecyclerView.Adapter<UserInfoRvAdapter.ViewHolder> {
    private OnClickCallBack callBack;
    private List<Map<String,String>> data;
    public UserInfoRvAdapter(List<Map<String,String>> data, OnClickCallBack callBack){
        this.callBack=callBack;
        this.data=data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_user_info,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String,String> map=data.get(position);
        holder.tvTitle.setText(map.get("title"));
        holder.tvValue.setText(map.get("value"));
        if (Boolean.valueOf(map.get("isAlter"))){
            holder.ivAlter.setVisibility(View.VISIBLE);
        }else{
            holder.ivAlter.setVisibility(View.INVISIBLE);
        }
        holder.position=position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvTitle;
        public TextView tvValue;
        public ImageView ivAlter;
        public int position;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle=(TextView)itemView.findViewById(R.id.user_info_title);
            tvValue=(TextView)itemView.findViewById(R.id.user_info_value);
            ivAlter=(ImageView)itemView.findViewById(R.id.user_info_alter);
            ivAlter.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (callBack!=null)
                callBack.onClick(v,position);
        }
    }
    public  interface  OnClickCallBack{
        void onClick(View v,int position);
    }
}
