package net.gocourse.model;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by loveyu on 2015/4/27 21:08. 教师基本信息
 */
public class TeacherInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public final static String TAG="TeacherInfo";
    //用户信息
    private BaseUserInfo user;
    //高校信息
    private CollegeInfo college;

    public BaseUserInfo getUser() {
        return user;
    }

    public void setUser(BaseUserInfo user) {
        this.user = user;
    }

    public CollegeInfo getCollege() {
        return college;
    }

    public void setCollege(CollegeInfo college) {
        this.college = college;
    }


    /**
     * 构造函数，从json中获取数据
     * @param json
     */
    public TeacherInfo(JSONObject json) {
        if (json != null) {
            user = BaseUserInfo.parseJson(json.getJSONObject("user"));
            college = CollegeInfo.parseJson(json.getJSONObject("college"));
        }

    }

    public TeacherInfo(){}

}
