package net.gocourse.net;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import net.gocourse.model.BaseJsonModel;
import net.gocourse.model.DataServerTime;
import net.gocourse.model.DataToken;
import net.gocourse.teacher.MyApplication;
import net.gocourse.util.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * User: xucan
 * Date: 2015-07-21
 * Time: 17:17
 * FIXME 更新Token值
 */
public class TokenUtil {
    //服务器时间
    private long serverSecond = 0;
    //token更新是否成功
    private boolean flag = false;
    //请求列队
    RequestQueue queue;
    MyApplication app;
    public TokenUtil(RequestQueue queue,MyApplication app){
        this.queue=queue;
        this.app=app;
    }

    /**
     * 获取服务器时间
     */
    public void getServerSecond() {
        StringRequest serverTimeRequest = MyStringRequest.getStringRequest(Constant.HTTP_METHOD_GET, Constant.getUrlStr(Constant.URL_SERVER_TIME), null, null, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                BaseJsonModel jsonModel = new BaseJsonModel(s);
                if (jsonModel.isStatus()) {
                    DataServerTime serverTime = DataServerTime.parseJson(jsonModel.getData());
                    serverSecond = serverTime.getSecond();
                }
            }
        },null);
        queue.add(serverTimeRequest);
    }

    /**
     * 更新Token
     * @return 更新结果
     */
    public boolean refreshToken() {
        getServerSecond();
        //请求头
        Map<String, String> header = new HashMap<String, String>();
        header.put("token", app.getDataToken().getToken());
        //post参数
        Map<String, String> params = new HashMap<String, String>();
        params.put("confirm", String.valueOf(serverSecond));

        StringRequest refreshRequest = MyStringRequest.getStringRequest(Constant.HTTP_METHOD_POST, Constant.getUrlStr(Constant.URL_REFRESH_TOKEN), params, header, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                BaseJsonModel jsonModel = new BaseJsonModel(s);
                if (jsonModel.isStatus()) {
                    DataToken token = DataToken.parseJson(jsonModel.getData());
                    //Token结果存放在MyApplication中
                    app.setDataToken(token);
                    flag = true;
                }
            }
        }, null);
        return flag;
    }

    /**
     * 检查token是否有效
     * @param token 已存在的token
     * @return
     */
    public boolean isValidToken(DataToken token){
        //获取服务器时间
        getServerSecond();
        //判断时间是否过期(保持24小时有效期)
        long expire=token.getExpire();
        if(serverSecond>expire){
            return false;
        }else{
            if (serverSecond>expire-60*60*24){
                //如果将在24小时内过期，则更新Token
                refreshToken();
            }
            return true;
        }
    }
}
