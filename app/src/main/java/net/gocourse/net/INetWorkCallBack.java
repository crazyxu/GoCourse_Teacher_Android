package net.gocourse.net;

/**
 * 网络请求结果的回调接口, 所有网络请求的类都要实现这个接口
 * @author Mouse
 */
public interface INetWorkCallBack {
    //处理网络请求前
    void onPreNetWork();

    //响应网络请求回调方法
    void onNetWorkResponse(int requestId, Object response);
}
