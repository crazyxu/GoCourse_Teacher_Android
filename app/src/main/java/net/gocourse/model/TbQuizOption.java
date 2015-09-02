package net.gocourse.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * User: xucan
 * Date: 2015-08-01
 * Time: 19:20
 * FIXME
 */
public class TbQuizOption implements Serializable {
    //选项描述
    private String description;

    //用户选择此选项为答案后，反馈给用户的信息
    private String feedback;

    //答案序号，对于ABC...
    private String index;

    //0-错误选项，1-正确选项
    private int isCorrect;
    private String optionID;
    private String quizID;

    public String getReply() {
        return reply;
    }

    public String getDescription() {
        return description;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getIndex() {
        return index;
    }

    public int getIsCorrect() {
        return isCorrect;
    }

    public String getOptionID() {
        return optionID;
    }

    public String getQuizID() {
        return quizID;
    }


    //回复数量
    private String reply;

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setIsCorrect(int isCorrect) {
        this.isCorrect = isCorrect;
    }

    public void setOptionID(String optionID) {
        this.optionID = optionID;
    }

    public void setQuizID(String quizID) {
        this.quizID = quizID;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    //未知
    private String correct;

    public static TbQuizOption parse(JSONObject obj){
        TbQuizOption tbQuizOption=JSON.parseObject(obj.toJSONString(), TbQuizOption.class);
        return tbQuizOption;
    }


}
