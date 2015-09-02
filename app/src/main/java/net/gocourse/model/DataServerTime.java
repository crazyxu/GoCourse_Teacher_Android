package net.gocourse.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * User: xucan
 * Date: 2015-07-21
 * Time: 17:25
 * FIXME 服务器端时间
 */
public class DataServerTime {
    //微秒
    private long millis;

    //秒
    private long second;

    //当前时区
    private String utc;

    //当前时区偏移毫秒
    private long utcOffset;

    //服务器时区
    private int utcNum;

    public long getMillis() {
        return millis;
    }public void setMillis(long millis) {
        this.millis = millis;
    }public long getSecond() {
        return second;
    }public void setSecond(long second) {
        this.second = second;
    }public String getUtc() {
        return utc;
    }public void setUtc(String utc) {
        this.utc = utc;
    }public int getUtcNum() {
        return utcNum;
    }public void setUtcNum(int utcNum) {
        this.utcNum = utcNum;
    }public long getUtcOffset() {
        return utcOffset;
    }public void setUtcOffset(long utcOffset) {
        this.utcOffset = utcOffset;
    }

    public static DataServerTime parseJson(JSONObject jsb) {
        return JSON.parseObject(jsb.toJSONString(), DataServerTime.class);
    }

}
