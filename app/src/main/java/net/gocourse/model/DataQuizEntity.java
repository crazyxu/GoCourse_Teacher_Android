package net.gocourse.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: xucan
 * Date: 2015-08-01
 * Time: 19:08
 * FIXME
 */
public class DataQuizEntity implements Serializable {
    private static final long serialVersionUID = 101L;
    public Object getComments() {
        return comments;
    }

    public List<TbQuizOption> getOptions() {
        return options;
    }

    public TbQuiz getQuiz() {
        return quiz;
    }

    //解析
    private Object comments;
    //选项
    private List<TbQuizOption> options;
    //题目
    private TbQuiz quiz;

    public DataQuizEntity(JSONObject obj){
        comments=obj.get("comments");
        options=new ArrayList<>();
        JSONArray array = obj.getJSONArray("options");
        for (int i=0;i<array.size();i++){
            TbQuizOption option=TbQuizOption.parse(array.getJSONObject(i));
            options.add(option);
        }
        quiz=TbQuiz.parse(obj.getJSONObject("quiz"));
    }
}
