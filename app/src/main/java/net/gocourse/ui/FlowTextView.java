package net.gocourse.ui;

import android.content.Context;
import android.os.Build;
import android.widget.TextView;

import net.gocourse.teacher.R;

/**
 * User: xucan
 * Date: 2015-08-13
 * Time: 09:32
 * FIXME
 */
public class FlowTextView{
    public final static int FLOW_TEXT_NORMAL=0;
    public final static int FLOW_TEXT_SMALL=1;
    public  int[] flowTextColor=new int[]{R.color.flow_text_view_bg_blue,R.color.flow_text_view_bg_green,
            R.color.flow_text_view_bg_grey,R.color.flow_text_view_bg_red,R.color.flow_text_view_bg_orange};
    public int[] flowTextDrawable=new int[]{R.drawable.flow_text_background_blue,R.drawable.flow_text_background_green,
                R.drawable.flow_text_background_grey,R.drawable.flow_text_background_red,R.drawable.flow_text_background_orange};
    public TextView getFlowTextView(Context context,int index,int size) {
        TextView tv=new TextView(context);
        FlowLayout.LayoutParams layoutParams=new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,FlowLayout.LayoutParams.WRAP_CONTENT);


        switch (size){
            case FlowTextView.FLOW_TEXT_NORMAL:
                layoutParams.setMargins(10, 10, 10, 10);
                tv.setPadding(10, 10, 10, 10);
                tv.setTextSize(18);
                break;
            case FlowTextView.FLOW_TEXT_SMALL:
                layoutParams.setMargins(5, 5, 5, 5);
                tv.setPadding(5, 5, 5, 5);
                tv.setTextSize(15);
                break;
        }

        tv.setLayoutParams(layoutParams);
        tv.setBackgroundColor(context.getResources().getColor(flowTextColor[index % flowTextColor.length]));
        if(Build.VERSION.SDK_INT >= 16){
            //android版本大于4.1,会有圆角效果
            tv.setBackgroundResource(flowTextDrawable[index % flowTextDrawable.length]);
        }
        tv.setTextColor(context.getResources().getColor(android.R.color.black));
        return tv;
    }
}
