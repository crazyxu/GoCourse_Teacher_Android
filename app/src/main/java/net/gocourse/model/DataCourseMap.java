package net.gocourse.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * User: xucan
 * Date: 2015-07-25
 * Time: 16:59
 * FIXME 教师的课程列表
 */
public class DataCourseMap {

    public static String TAG="DataCourseMap";
    public Map<String,String> getCourseMap() {
        return courseMap;
    }

    private Map<String,String> courseMap;

    public DataCourseMap(){}

    public DataCourseMap(JSONObject obj){
        courseMap=(Map<String,String>)JSON.parse(obj.toJSONString());
    }



}
