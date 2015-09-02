package net.gocourse.model;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * User: xucan
 * Date: 2015-07-30
 * Time: 19:02
 * 评论数据
 */
public class DataReview {
    public List<TbReviewReply> getReplies() {
        return replies;
    }

    public TbReview getReview() {
        return review;
    }

    //教师回复列表
    private List<TbReviewReply> replies;
    //学生评论
    private TbReview review;

    //增加教师回复
    public void addReply(TbReviewReply reply){
        replies.add(reply);
    }


    public DataReview(JSONObject obj){
        JSONArray array=obj.getJSONArray("replies");
        replies=new ArrayList<>();
        for (int i=0;i<array.size();i++){
            replies.add(TbReviewReply.parseJson(array.getJSONObject(i)));
        }
        review=TbReview.parseJson(obj.getJSONObject("review"));
    }
}
