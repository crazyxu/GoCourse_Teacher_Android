package net.gocourse.function.user;/**
 * Created by xc on 2015/8/13.
 * GoCourse_Teacher_Android
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gocourse.model.TbCourseLocation;
import net.gocourse.teacher.R;
import net.gocourse.util.Constant;

import java.util.List;

/**
 * User: xucan
 * Date: 2015-08-13
 * Time: 13:06
 * FIXME
 */
public class MyUserLocationRvAdapter extends RecyclerView.Adapter<MyUserLocationRvAdapter.ViewHolder>{
    private List<TbCourseLocation> locationList;
    public MyUserLocationRvAdapter(List<TbCourseLocation> locationList){
        this.locationList = locationList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_course_detail_location,parent,false);
        ViewHolder holder=new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TbCourseLocation location=locationList.get(position);
        holder.tvDay.setText(Constant.ARR_WEEK_NAME[location.getDay()-1]);
        holder.tvLocation.setText(location.getLocation());
        holder.tvSlot.setText("第"+location.getSlot()+"节");
        holder.tvWeek.setText("周次："+location.getWeek());
    }

    @Override
    public int getItemCount() {
        if (locationList!=null)
            return locationList.size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvLocation;
        public TextView tvDay;
        public TextView tvWeek;
        public TextView tvSlot;
        public ViewHolder(View itemView) {
            super(itemView);
            tvLocation=(TextView)itemView.findViewById(R.id.tv_location_name);
            tvDay=(TextView)itemView.findViewById(R.id.tv_location_day);
            tvWeek=(TextView)itemView.findViewById(R.id.tv_location_week);
            tvSlot=(TextView)itemView.findViewById(R.id.tv_location_slot);
        }
    }
}
