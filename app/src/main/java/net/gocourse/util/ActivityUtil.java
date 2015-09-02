package net.gocourse.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import net.gocourse.teacher.R;

public class ActivityUtil {

    public static void goNextActivity(Context context, Class clazz,
                                      Bundle bundle,Boolean finish){
        Intent intent = new Intent(context, clazz);
        if(bundle!=null)
            intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_left_in,
                R.anim.fade_out_long_animation);
        if (finish)
            //结束当前Activity
            ((Activity) context).finish();
    }




    public static void showShortToast(Context mContext, String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context mContext, String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    public static void goNextActivityForResult(Context context, Class clazz,
                                               Bundle bundle, int requestCode) {
        Intent intent = new Intent(context, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ((Activity) context).startActivityForResult(intent, requestCode);
        ((Activity) context).overridePendingTransition(R.anim.slide_left_in,
                R.anim.fade_out_long_animation);
    }

}
