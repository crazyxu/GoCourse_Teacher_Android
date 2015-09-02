package net.gocourse.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * User: xucan
 * Date: 2015-07-30
 * Time: 19:04
 * 教师对学生评价的回复
 */
public class TbReviewReply {
    //回复内容
    private String content;
    //评价ID
    public String getContent() {
        return content;
    }
    public int getReviewID() {
        return reviewID;
    }
    public int getReviewReplyID() {
        return reviewReplyID;
    }
    public long getTime() {
        return time;
    }
    public int getUserID() {
        return userID;
    }private int reviewID;
    //回复ID
    private int reviewReplyID;
    //回复时间
    private long time;
    //教师ID
    private int userID;

    public void setContent(String content) {
        this.content = content;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public void setReviewReplyID(int reviewReplyID) {
        this.reviewReplyID = reviewReplyID;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public static TbReviewReply parseJson(JSONObject obj){
        return JSON.parseObject(obj.toJSONString(), TbReviewReply.class);
    }
}
