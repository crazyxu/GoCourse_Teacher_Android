package net.gocourse.function.course;

import android.os.Bundle;

import net.gocourse.model.DataCourseTable;
import net.gocourse.model.TbCourseLocation;
import net.gocourse.model.TbCourseTableSearch;

import java.util.List;

/**
 * User: xucan
 * Date: 2015-08-01
 * Time: 17:50
 * FIXME
 */
public class CourseDataUtil {
    public static Bundle getCourseTableData(int week,int day,int slot,List<DataCourseTable> data){
        Bundle bundle = new Bundle();
        DataCourseTable courseTable;
        List<TbCourseLocation> locations;
        TbCourseLocation location;
        TbCourseTableSearch course;
        String classes = "";
        //所有课程
        for (int i=0;i<data.size();i++){
            courseTable = data.get(i);
            locations = courseTable.getLocations();
            //所有上课节次（一般为一或二），即一周该课程上的次数
            for (int j = 0;j < locations.size();j++){
                location = locations.get(j);
                //判断是否和当前星期数和节次书相符，day和slot从0开始
                if ((location.getDay() == (day+1)&&(location.getSlot()==(slot+1)))){
                    //如果该周次上此课
                    if (isCurWeek(location.getWeek(),week)){
                        course = courseTable.getCourse();
                        //获取数据
                        bundle.putInt("courseId",course.getCourseID());
                        bundle.putString("courseName",course.getCourseName());
                        bundle.putString("location", location.getLocation());
                        bundle.putInt("courseTableId", course.getCourseTableID());
                        for (int k=0;k<courseTable.getClasses_info().size();k++){
                            classes += courseTable.getClasses_info().get(k).getClassName();
                        }
                        bundle.putString("classes",classes);
                        //匹配到一个后，不再继续
                        bundle.putBoolean("has",true);
                        return bundle;
                    }
                }
            }
        }
        bundle.putBoolean("has",false);
        return bundle;
    }

    /**
     * 根据给定周数数据（格式不确定），判断是否包括当前周
     * @param week
     * @param thisWeek
     * @return
     */
    static boolean isCurWeek(String week , int thisWeek){
        String[] arrWeek;
        arrWeek=week.split("-");
        //如果arrWeek长度为2表示使用了"-"分隔符，例如"1-18"周
        if (arrWeek.length==2){
            if ((Integer.valueOf(arrWeek[0])<=(thisWeek))&&
                    (Integer.valueOf(arrWeek[1])>=(thisWeek))){
                return true;
            }
        }else{
            //使用"."分隔符,例如"1,3,5,7,9,11"
            arrWeek=week.split(",");
            for (int k = 0; k < arrWeek.length; k++) {
                //如果该地点下该周有课程
                if (arrWeek[k].equals(String.valueOf(thisWeek))){
                    return true;
                }
            }
        }
        return false;
    }


}
