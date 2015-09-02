package net.gocourse.function.signIn;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.readystatesoftware.viewbadger.BadgeView;

import net.gocourse.model.TbSignBasicData;
import net.gocourse.model.TbSignData;
import net.gocourse.teacher.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * User: xucan
 * Date: 2015-07-28
 * Time: 20:49
 * FIXME
 */
public class SignInRvAdapter extends RecyclerView.Adapter<SignInRvAdapter.ViewHolder>{

    private List<TbSignData> data;
    private Context context;
    private BtnCallBack callBack;
    public SignInRvAdapter(List<TbSignData> data, Context context, BtnCallBack callBack){
        this.data=data;
        this.context=context;
        this.callBack=callBack;
    }

    @Override
    public SignInRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item_sign_in, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //设值
        TbSignBasicData signData=data.get(position).getBasicData();
        holder.tvName.setText(signData.getName());
        holder.tvDetail.setText(signData.getDetail());
        //btnClick事件
        final int id=position;
//        holder.btnClick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                callBack.onClick(id);
//            }
//        });
        //显示签到人数
        BadgeView badge = new BadgeView(context,holder.tvCount);
        badge.setText(signData.getCount()+"");
        badge.setBadgeBackgroundColor(context.getResources().getColor(R.color.badge_sign_count_background_color));
        //居中
        badge.setBadgePosition(BadgeView.POSITION_CENTER);
        badge.setTextSize(16);
        badge.show();
        //时间
        SimpleDateFormat sdf=new SimpleDateFormat("MM月dd日 HH:mm");
        String startTime = sdf.format(new Date(1000*signData.getTime()));
        String expireTime = sdf.format(new Date(1000*signData.getExpireTime()));
        holder.tvTime.setText(startTime+" "+expireTime);
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvName;
        public TextView tvDetail;
        public TextView tvCount;
        public TextView tvTime;
        //public Button btnClick;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName=(TextView)itemView.findViewById(R.id.tv_sign_name);
            tvDetail=(TextView)itemView.findViewById(R.id.tv_sign_detail);
            tvCount=(TextView)itemView.findViewById(R.id.tv_sign_count);
            tvTime=(TextView)itemView.findViewById(R.id.tv_sign_time);
            //btnClick=(Button)itemView.findViewById(R.id.btn_sign_click);
        }
    }

    //回调函数，响应button事件
    interface BtnCallBack{
        void onClick(int position);
    }
}
