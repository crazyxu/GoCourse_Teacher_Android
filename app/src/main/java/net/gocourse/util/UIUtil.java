package net.gocourse.util;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * User: xucan
 * Date: 2015-07-28
 * Time: 10:10
 * FIXME
 */
public class UIUtil {

    /**
     * 简单的SnackBar
     * @param view
     * @param content
     * @param isLong 是否LENGTH_LONG
     */
    public static void showSnackBar(View view,String content,boolean isLong){
        Snackbar snackbar = Snackbar.make(view,content,Snackbar.LENGTH_SHORT);
        if (isLong)
            snackbar.setDuration(Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /**
     * 带点击事件的SnackBar
     * @param view
     * @param content
     * @param actionTxt action文本
     * @param isLong 是否LENGTH_LONG
     * @param listener 点击事件
     */
    public static void showSnackBar(View view,String content,String actionTxt,boolean isLong,View.OnClickListener listener) {
        Snackbar snackbar = Snackbar.make(view, content, Snackbar.LENGTH_SHORT);
        if (isLong) {
            snackbar.setDuration(Snackbar.LENGTH_SHORT);
            snackbar.setAction(actionTxt, listener);
            snackbar.show();

        }
    }

}
