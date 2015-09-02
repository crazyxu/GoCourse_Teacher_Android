package net.gocourse.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * User: xucan
 * Date: 2015-07-30
 * Time: 19:14
 * 简单用户信息
 */
public class TbUserSimple {
    public String getUserAvatar() {
        return userAvatar;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserSysID() {
        return userSysID;
    }

    //用户头像地址
    private String userAvatar;
    private String userID;
    private String userName;

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserSysID(int userSysID) {
        this.userSysID = userSysID;
    }

    private int userSysID;

    public static TbUserSimple parseJson(JSONObject obj){
        return JSON.parseObject(obj.toJSONString(), TbUserSimple.class);
    }
}
