package net.gocourse.function.course;
/**
 * User: xucan
 * Date: 2015-07-21
 * Time: 09:46
 * FIXME 顶部滑动菜单
 * @deprecated
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.gocourse.teacher.R;

import java.util.List;
public class TabView extends LinearLayout {

    private int screenWidth;
    private String[] tabNames;
    private int fontSize;
    private TextView[] mTextViews;

    private int lineHeight;
    private View sliderLine;
    private int tabWidth;
    private int selectedPage;

    public TabView(Context context) {
        super(context);
        init(context);
    }

    public TabView(Context context, String[] tabNames, DisplayMetrics displayMetrics, int fontSize) {
        super(context);
        this.tabNames = tabNames;
        screenWidth = displayMetrics.widthPixels;
        this.fontSize = fontSize;

        init(context);
    }

    public TabView(Context context, List<String> names, DisplayMetrics displayMetrics, int fontSize) {
        super(context);
        if (names != null) {
            tabNames = new String[names.size()];
            for (int i = 0; i < tabNames.length; i++) {
                this.tabNames[i] =names.get(i);
            }
        }
        screenWidth = displayMetrics.widthPixels;
        this.fontSize = fontSize;
        init(context);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @SuppressLint("NewApi")
    public TabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        if (tabNames==null || tabNames.length == 0) {
            return;
        }
        lineHeight = (int) getResources().getDimension(R.dimen.cursor_height);
        tabWidth = screenWidth / tabNames.length;
        mTextViews = new TextView[tabNames.length];
        setOrientation(LinearLayout.VERTICAL);


        LinearLayout.LayoutParams layoutParams =new LayoutParams(screenWidth / tabNames.length,
                LayoutParams.MATCH_PARENT);
        LinearLayout linearLayout = new LinearLayout(context);
        int height = (int) getResources().getDimension(R.dimen.tab_height);
        linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,height));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < tabNames.length; i++) {
            TextView textView = new TextView(context);
            textView.setText(tabNames[i] + "");
            textView.setLayoutParams(layoutParams);
            textView.setTextSize(fontSize);
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER);
            mTextViews[i] = textView;
            final int pos = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectedPage!=pos){
                        isHidden(pos);
                    }
                    if (tabViewListener != null) {
                        tabViewListener.currentPos(pos);
                    }
                }
            });
            linearLayout.addView(textView);
        }
        addView(linearLayout);
        if (tabNames.length>1) {
            LinearLayout.LayoutParams lineLayoutParams = new LinearLayout.LayoutParams(tabWidth, lineHeight);
            TextView textView = new TextView(context);
            textView.setBackgroundColor(Color.parseColor("#FA8653"));
            textView.setLayoutParams(lineLayoutParams);
            sliderLine=textView;
            addView(textView);
        }
        isHidden(0);
    }


    private TabViewListener tabViewListener;

    public void setTabViewListener(TabViewListener linListener) {
        this.tabViewListener = linListener;
    }
    public TabViewListener getTabViewListener(){
        return tabViewListener;
    }
    public interface TabViewListener {
        public void currentPos(int pos);
    }

    /*
     * 切换字体颜色
     */
    public void isHidden(int pos) {
        if (pos >= 0 || pos < tabNames.length) {
            tabScroll(pos);
            mTextViews[selectedPage].setTextColor(Color.BLACK);
            mTextViews[pos].setTextColor(Color.parseColor("#FA8653"));
            selectedPage=pos;
        }
    }

    /*
     * 滚动动画
     */
    private void tabScroll(int index){
        if (selectedPage==index) {
            return;
        }
        sliderLine.setTranslationX(0);
        TranslateAnimation animation = new TranslateAnimation(selectedPage
                * tabWidth, index * tabWidth, 0, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(150);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        sliderLine.startAnimation(animation);
        animation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sliderLine.clearAnimation();
                sliderLine.setTranslationX(selectedPage * tabWidth);
            }
        });

    }
}