package net.gocourse.net;

/**
 * 子线程处理回调
 * @author xucan
 */
public interface SubThreadCallBack {
    //执行前
    void onPreExecute(int requestId);

    //执行方法
    Object doInBackground(int requestId);

    //执行后
    void onPostExecute(int responseId,Object result);
}
