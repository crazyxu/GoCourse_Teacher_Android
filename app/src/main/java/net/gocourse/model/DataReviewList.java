package net.gocourse.model;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: xucan
 * Date: 2015-07-30
 * Time: 19:01
 * 课程评论信息
 */
public class DataReviewList implements Cloneable {
    //是否允许评论（学生端）
    private boolean allowComment;
    //是否允许回复（教师端）
    private boolean allowReply;

    public boolean isAllowComment() {
        return allowComment;
    }

    public boolean isAllowReply() {
        return allowReply;
    }

    public List<DataReview> getList() {
        return list;
    }

    public Map<String, TbUserSimple> getUser() {
        return user;
    }

    private List<DataReview> list;
    private Map<String,TbUserSimple> user;

    public DataReviewList(JSONObject obj) {
        list=new ArrayList<>();
        user=new HashMap<>();
        JSONArray array=obj.getJSONArray("list");
        for (int i=0;i<array.size();i++){
            list.add(new DataReview(array.getJSONObject(i)));
        }
        allowComment=obj.getBoolean("allowComment");
        allowReply=obj.getBoolean("allowReply");

        //先解析成Map<String,String>
        Map<String,JSONObject> map=(Map<String,JSONObject>)JSON.parse(obj.getString("users"));
        //然后把String类型value解析成对象
        for (String key : map.keySet()){
            TbUserSimple userSimple=TbUserSimple.parseJson(map.get(key));
            user.put(key,userSimple);
        }
    }

    //增加教师回复
    public void addReply(TbReviewReply reply,int position){
        list.get(position).addReply(reply);
    }
    //增加赞或踩
    public void addLike(int position,boolean up){
        list.get(position).getReview().addLike(up);
    }

    public void setValue(DataReviewList dataReviewList){
        allowComment = dataReviewList.allowComment;
        allowReply = dataReviewList.allowReply;
        list = dataReviewList.list;
        user= dataReviewList.user;

    }

    public DataReviewList(){}

    //以便可以进行对象拷贝
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
}
