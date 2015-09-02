package net.gocourse.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

/**
 * User: xucan
 * Date: 2015-07-24
 * Time: 11:26
 * FIXME
 */
public class ListDialog {
    public static AlertDialog.Builder getDialog(final Context mContext, String[] arrData, final DialogCallBack callBack){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
        ArrayAdapter<Object> arrayAdapter = new ArrayAdapter<Object>(mContext,
                android.R.layout.simple_list_item_1,arrData);
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.onClick(dialog,which);
            }
        });
        return builderSingle;

    }





    public interface DialogCallBack{
        void onClick(DialogInterface dialog, int which);
    }

}
