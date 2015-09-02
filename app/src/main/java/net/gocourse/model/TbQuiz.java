package net.gocourse.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * User: xucan
 * Date: 2015-08-01
 * Time: 19:08
 * FIXME
 */
public class TbQuiz implements Serializable {
    public String getCourseId() {
        return courseId;
    }

    public String getDesc() {
        return desc;
    }

    public String getIndex() {
        return index;
    }

    public int getQuizID() {
        return quizID;
    }

    public String getReply() {
        return reply;
    }

    public int getStatus() {
        return status;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public long getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    private String courseId;

    //测验描述信息，即解析信息
    private String desc;

    //章节信息
    private String index;

    private int quizID;

    //该题目回复的数量
    private String reply;

    //正确答案的个数，保证0,-1,-2是一些特殊保留字，比如判断题的选项
    private String size;

    //状态，0-私有，1-共享，2-删除
    private int status;

    private String teacherID;

    //发布时间
    private long time;

    //测验标题
    private String title;

    //测试的类型，0:单选，1:多选，2:判断
    private String type;

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getMulti() {
        return multi;
    }

    public void setMulti(String multi) {
        this.multi = multi;
    }

    public void setQuizID(int quizID) {
        this.quizID = quizID;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getSingle() {
        return single;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String multi;

    private String share;

    private String single;

    public static TbQuiz parse(JSONObject obj){
        return JSON.parseObject(obj.toJSONString(),TbQuiz.class);
    }
}
