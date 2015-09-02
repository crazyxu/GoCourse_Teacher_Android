package net.gocourse.model;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * User: xucan
 * Date: 2015-07-27
 * Time: 19:05
 * 签到任务附加信息
 */
public class DataAppendInfo {
    private String content;
    private int time;
    public String getContent() {
        return content;
    }

    public int getTime() {
        return time;
    }

    public static DataAppendInfo getInfo(JSONObject obj){
        return JSON.parseObject(obj.toJSONString(),DataAppendInfo.class);

    }


}
