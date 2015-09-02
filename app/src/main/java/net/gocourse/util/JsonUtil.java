package net.gocourse.util;/**
 * Created by xc on 2015/8/5.
 * GoCourse_Teacher_Android
 */

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * User: xucan
 * Date: 2015-08-05
 * Time: 11:23
 * FIXME
 */
public class JsonUtil {
    public static String getJsonStr(List<String> correct, String des, String index, List<String> options, String title) {
        JSONObject object = new JSONObject();
        object.put("title", title);
        object.put("options", options);
        object.put("correct", correct);
        object.put("desc", des);
        object.put("index", index);
        return object.toJSONString();
    }
}
