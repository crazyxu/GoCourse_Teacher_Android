package net.gocourse.ui;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.gocourse.teacher.R;

/**
 * User: xucan
 * Date: 2015-08-03
 * Time: 15:24
 * FIXME
 */
public class QuizOptionDisplayView extends LinearLayout{
    private String[] arr=new String[]{"A","B","C","D","E","F","G","H"};

    //选项index,即A,B,C,D...
    private TextView optionIndex;
    private TextView optionText;
    private View rootView;

    public QuizOptionDisplayView(Context context, int index ,boolean isChecked,String content) {
        super(context);
        this.setOrientation(LinearLayout.HORIZONTAL);
        rootView = LayoutInflater.from(context).inflate(R.layout.include_quiz_detail_option,null);
        optionIndex = (TextView)rootView.findViewById(R.id.quiz_option_index);
        optionText = (TextView)rootView.findViewById(R.id.quiz_option_content);

        if (isChecked){
            optionIndex.setText("√");
        }else{
            optionIndex.setText(arr[index]);
        }
        optionText.setText(content);

        this.addView(rootView);
    }


}
