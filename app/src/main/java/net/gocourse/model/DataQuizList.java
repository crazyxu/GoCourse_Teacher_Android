package net.gocourse.model;/**
 * Created by xc on 2015/8/1.
 * GoCourse_Teacher_Android
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * User: xucan
 * Date: 2015-08-01
 * Time: 19:05
 * FIXME
 */
public class DataQuizList {
    public List<DataQuizEntity> getList() {
        return list;
    }

    private List<DataQuizEntity> list;

    public DataQuizList(JSONObject obj){
        list=new ArrayList<>();
        JSONArray array = obj.getJSONArray("list");
        for (int i=0;i<array.size();i++){
            list.add(new DataQuizEntity(array.getJSONObject(i)));
        }
    }

}
