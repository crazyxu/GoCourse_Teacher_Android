package net.gocourse.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * User: xucan
 * Date: 2015-08-23
 * Time: 14:46
 * FIXME
 */
public class DataEmailStatus {
    //邮箱
    private String email;
    //0未验证，1已验证
    private int status;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public static DataEmailStatus parse(JSONObject object){
        return JSON.parseObject(object.toJSONString(), DataEmailStatus.class);
    }
}
