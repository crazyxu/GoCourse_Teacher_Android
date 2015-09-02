package net.gocourse.net;
import android.os.AsyncTask;
/**
 * 子线程处理
 */
public class SubThreadTask extends AsyncTask<Object, Integer, Object> {
    //网络请求回调
    private SubThreadCallBack subThreadCallBack;
    //请求id
    private int requestId;

    private static String TAG = "SubThreadTask";

    public SubThreadTask(SubThreadCallBack subThreadCallBack, int requestId) {
        this.subThreadCallBack=subThreadCallBack;
        this.requestId=requestId;
    }

    @Override
    protected void onPreExecute() {
        subThreadCallBack.onPreExecute(requestId);
    }

    @Override
    protected Object doInBackground(Object... params) {
        return subThreadCallBack.doInBackground(requestId);
    }

    @Override
    protected void onPostExecute(Object response) {
        subThreadCallBack.onPostExecute(requestId,response);
    }
}