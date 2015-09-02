package net.gocourse.teacher;

import android.app.Application;

import net.gocourse.model.DataCourseTableSearch;
import net.gocourse.model.DataToken;

/**
 * @author: xucan
 * Date: 2015-05-20
 * Time: 10:07
 * 用于存放全局变量
 */
public class MyApplication extends Application {
    //app主题，0表示默认Day，1表示Night
    private int appTheme=0;

    public int getAppTheme() {
        return appTheme;
    }

    public void setAppTheme(int appTheme) {
        this.appTheme = appTheme;
    }

    public DataToken getDataToken() {
        return dataToken;
    }

    public void setDataToken(DataToken dataToken) {
        this.dataToken = dataToken;
    }

    private DataToken dataToken;

    public DataCourseTableSearch getDataCourseTableSearch() {
        return dataCourseTableSearch;
    }

    public void setDataCourseTableSearch(DataCourseTableSearch dataCourseTableSearch) {
        this.dataCourseTableSearch = dataCourseTableSearch;
    }

    private DataCourseTableSearch dataCourseTableSearch;

    public int getSelectWeek() {
        return selectWeek;
    }

    public void setSelectWeek(int selectWeek) {
        this.selectWeek = selectWeek;
    }

    //当前选择的周数
    private int selectWeek;

    public int getThisWeek() {
        return thisWeek;
    }

    public void setThisWeek(int thisWeek) {
        this.thisWeek = thisWeek;
    }

    //当前周数
    private int thisWeek;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    void clear(){
        appTheme=0;
        dataToken=null;
        dataCourseTableSearch=null;
        selectWeek=0;
        thisWeek=0;
    }

}
