package net.gocourse.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * User: xucan
 * Date: 2015-07-30
 * Time: 19:08
 * 学生对教师的评论
 */
public class TbReview {
    private String content;
    private int courseTableID;
    private int downNum;

    public int getUserID() {
        return userID;
    }

    public String getContent() {
        return content;
    }

    public int getCourseTableID() {
        return courseTableID;
    }

    public int getDownNum() {
        return downNum;
    }

    public float getRating() {
        return rating;
    }

    public int getReviewID() {
        return reviewID;
    }

    public long getTime() {
        return time;
    }

    public int getUpNum() {
        return upNum;
    }

    private float rating;

    public void setContent(String content) {
        this.content = content;
    }

    public void setCourseTableID(int courseTableID) {
        this.courseTableID = courseTableID;
    }

    public void setDownNum(int downNum) {
        this.downNum = downNum;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setUpNum(int upNum) {
        this.upNum = upNum;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    private Integer reviewID;
    private long time;
    private Integer upNum;
    private Integer userID;

    public TbReview(){}

    public static TbReview parseJson(JSONObject obj){
        return JSON.parseObject(obj.toJSONString(), TbReview.class);
    }

    //增加赞或者踩
    public void addLike(boolean up){
        if (up){
            upNum+=1;
        }else{
            downNum+=1;
        }
    }

}
