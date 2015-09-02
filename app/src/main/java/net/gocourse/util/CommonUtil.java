package net.gocourse.util;/**
 * Created by xc on 2015/7/27.
 * GoCourse_Teacher_Android
 */

import android.os.Bundle;

import net.gocourse.model.CourseTableIdWithClass;
import net.gocourse.model.DataCourseTable;
import net.gocourse.model.DataToken;
import net.gocourse.model.TbCourseLocation;
import net.gocourse.teacher.MyApplication;
import net.gocourse.ui.WebViewActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: xucan
 * Date: 2015-07-27
 * Time: 15:23
 * FIXME
 */
public class CommonUtil {
    //获取逆转Map，必须保证key-value都唯一，
    public static Map<String,String> getInverseMap(Map<String,String> originMap){
        Map<String,String> map=new HashMap<>();
        for(String key : originMap.keySet()){
            map.put(originMap.get(key),key);
        }
        return map;
    }

    /**
     * 通过courseId获取课表对象信息
     * @param courseId
     * @param courseList
     * @return dataCourseList
     */
    public static List<DataCourseTable> getByCourseId(List<DataCourseTable> courseList, int courseId){
        List<DataCourseTable> dataCourseList=new ArrayList<DataCourseTable>();
        for (int i=0;i<courseList.size();i++){
            if(courseList.get(i).getCourse().getCourseID()==courseId){
                dataCourseList.add(courseList.get(i));
            }
        }
        return dataCourseList;
    }

    /**
     * 工具courseTabledId 来获取DataCourseTable
     * @param courseList
     * @param courseTabledId
     * @return
     */
    public static DataCourseTable getByCourseTableId(List<DataCourseTable> courseList, int courseTabledId){
        DataCourseTable dataCourse=null;
        List<TbCourseLocation> locations;
        TbCourseLocation location;
        for (int i=0;i<courseList.size();i++){
            dataCourse=courseList.get(i);
            locations=dataCourse.getLocations();
            for (int j=0;j<locations.size();j++){
                location=locations.get(j);
                if (location.getCourseTableID()==courseTabledId)
                    return dataCourse;
            }

        }
        return null;
    }

    public static Map<String,String> getTokenMap(MyApplication app){
        Map<String,String> header=new HashMap<>();
        DataToken dataToken=app.getDataToken();
        if (dataToken!=null)
            header.put("token",dataToken.getToken());
        return header;
    }

    /**
     * 通过value寻找key，需要保证value唯一
     * @param map
     * @param value
     * @return
     */
    public static String getKeyByValue(Map<String,String> map,String value){
        for(String key : map.keySet()){
            if (map.get(key).equals(value))
                return key;
        }
        return null;
    }

    /**
     * 获取课程表map，courseTableId->(courseTableName+classesName)
     * @param courseList
     * @return
     */
    public static Map<String,String> getCourseTableMap(List<DataCourseTable> courseList){
        Map<String,String> map=new HashMap<>();
        for (int i=0;i<courseList.size();i++){
            DataCourseTable courseTable=courseList.get(i);
            String courseTableName=courseTable.getCourse().getCourseName();
            int courseTableId=courseTable.getCourse().getCourseTableID();
            List<CourseTableIdWithClass> classes=courseTable.getClasses_info();
            String classesName="";
            for (int j=0;j<classes.size();j++){
                classesName+=classes.get(j).getClassName();
            }
            map.put(String.valueOf(courseTableId),courseTableName+"("+classesName+")");
        }
        return map;

    }

    /**
     * 根据courseId获取课程表map，courseTableId->(courseTableName+classesName)
     * @param courseList
     * @param courseId
     * @return
     */
    public static Map<String,String> getCourseTableMap(List<DataCourseTable> courseList,int courseId){
        Map<String,String> map=new HashMap<>();
        for (int i=0;i<courseList.size();i++){
            DataCourseTable courseTable=courseList.get(i);
            if(courseTable.getCourse().getCourseID()==courseId){
                int courseTableId=courseTable.getCourse().getCourseTableID();
                List<CourseTableIdWithClass> classes=courseTable.getClasses_info();
                String classesName="";
                for (int j=0;j<classes.size();j++){
                    classesName+=classes.get(j).getClassName();
                }
                map.put(String.valueOf(courseTableId),"("+classesName+")");
            }
        }
        return map;

    }

    public static String getCutWeekDay(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day==0)
            day=6;
        else
            day-=1;
        return Constant.ARR_WEEK_NAME[day];
    }

    public static Bundle getWebViewCookieBundle(MyApplication app,String url){
        Bundle bundle=new Bundle();
        bundle.putString("url", url);
        WebViewActivity.MyCookies myCookies=new WebViewActivity.MyCookies();
        myCookies.setCookies(getTokenMap(app));
        bundle.putSerializable("cookies",myCookies);
        return bundle;
    }


}
