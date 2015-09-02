package net.gocourse.util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import net.gocourse.model.BaseJsonModel;
import net.gocourse.model.DataCourseMap;
import net.gocourse.net.MyStringRequest;
import net.gocourse.teacher.MyApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * User: xucan
 * Date: 2015-08-10
 * Time: 17:05
 * FIXME
 */
public class CourseDataUtil {

    public static void getCourseMap(RequestQueue queue,final Context context,MyApplication app, final CourseDataCallBack callBack){
        DataCourseMap courseMap = null;
        //从本地sp获取
        CodecModelUtil cmu = new CodecModelUtil(context,Constant.SP_Course,courseMap,DataCourseMap.TAG);
        courseMap=(DataCourseMap)cmu.readFromSp();
        callBack.onFinish(courseMap);
        //如果本地不存在，则从网络获取
        if (courseMap==null){
            String url= Constant.getUrlStr(Constant.URL_COURSE_LIST);
            Map<String,String> params=new HashMap<String,String>();
            params.put("status", "-1");
            StringRequest courseListRequest= MyStringRequest.getStringRequest(Constant.HTTP_METHOD_POST,
                    url, params, CommonUtil.getTokenMap(app), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            BaseJsonModel jsonModel = new BaseJsonModel(s);
                            if (jsonModel.isStatus()){
                                //提取DataCourseMap数据并保存到本地
                                DataCourseMap courseMap = new DataCourseMap(jsonModel.getData());
                                CodecModelUtil cmu = new CodecModelUtil(context,Constant.SP_Course,courseMap,DataCourseMap.TAG);
                                cmu.saveToSp();
                                //其他回调操作
                                callBack.onFinish(courseMap);
                            }
                        }
                    }, null);
            queue.add(courseListRequest);
        }
    }

    public interface CourseDataCallBack{
        void onFinish(DataCourseMap dataCourseMap);
    }

}
