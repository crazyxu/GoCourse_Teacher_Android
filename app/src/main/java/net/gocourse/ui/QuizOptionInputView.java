package net.gocourse.ui;/**
 * Created by xc on 2015/8/3.
 * GoCourse_Teacher_Android
 */

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * User: xucan
 * Date: 2015-08-03
 * Time: 15:24
 * FIXME
 */
public class QuizOptionInputView extends LinearLayout{
    private String[] arr=new String[]{"A","B","C","D","E","F","G","H"};
    //该选项是否被选中
    private boolean isChecked;
    //选项内容
    private String text;

    public int getIndex() {
        return index;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public String getText() {
        return text;
    }


    //选项index,即A,B,C,D...
    private int index;
    private CheckBox optionBox;
    private EditText optionText;

    public QuizOptionInputView(Context context, int index) {
        super(context);
        this.index=index;
        this.setOrientation(LinearLayout.HORIZONTAL);
        optionBox=new CheckBox(context);
        optionText=new EditText(context);
        optionText.setHint("请输入选项"+arr[index]+"答案");
        optionText.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        this.addView(optionBox);
        this.addView(optionText);

        optionBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                QuizOptionInputView.this.isChecked = isChecked;
            }
        });

        optionText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                text=s.toString();
            }
        });
    }

}
