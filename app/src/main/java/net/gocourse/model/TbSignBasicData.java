package net.gocourse.model;/**
 * Created by xc on 2015/7/27.
 * GoCourse_Teacher_Android
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;


/**
 * User: xucan
 * Date: 2015-07-27
 * Time: 19:26
 * FIXME
 */
public class TbSignBasicData implements Serializable {
    private static final long serialVersionUID = 112L;
    //签到总人数
    private int count;
    private int courseID;
    private int courseTableID;


    public int getCount() {
        return count;
    }

    public int getCourseID() {
        return courseID;
    }

    public int getCourseTableID() {
        return courseTableID;
    }

    public String getDetail() {
        return detail;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public int getFlag() {
        return flag;
    }

    public String getName() {
        return name;
    }



    public int getSignID() {
        return signID;
    }

    public int getTaskID() {
        return taskID;
    }

    public long getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

    public int getUserID() {
        return userID;
    }

    //签到详细信息
    private String detail;
    //签到有效期
    private long expireTime;
    //任务状态:0正常,1删除
    private int flag;
    //任务名称
    private String name;
    private int signID;
    private  int taskID;
    private long time;
    //任务类型：0签到，1课程测验
    private int type;
    private int userID;

    public TbSignBasicData(){
    }

    public TbSignBasicData(JSONObject obj){
        detail=obj.getString("detail");
        name=obj.getString("name");
        time=obj.getLong("time");
        expireTime=obj.getLong("expireTime");
        count=obj.getIntValue("count");
        userID=obj.getIntValue("userID");
        courseID =obj.getIntValue("courseID");
        courseTableID =obj.getIntValue("courseTableID");
        flag=obj.getIntValue("flag");
        signID=obj.getIntValue("signID");
        taskID=obj.getIntValue("taskID");
        type=obj.getIntValue("type");

    }

    public static TbSignBasicData getBasicData(JSONObject obj){
        return JSON.parseObject(obj.toJSONString(),TbSignBasicData.class);
    }
}
