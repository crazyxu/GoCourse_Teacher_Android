package net.gocourse.model;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 用户验证
 */
public class DataToken implements Serializable{

    //token,用户在线标识，定时更新
    public String token;
    //expire，token过期时间戳
    public long expire;
    //序列化
    private static final long serialVersionUID = 123L;

    public static DataToken parseJson(JSONObject jsb) {
        return JSON.parseObject(jsb.toJSONString(), DataToken.class);
    }

    public DataToken() {
        super();
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
