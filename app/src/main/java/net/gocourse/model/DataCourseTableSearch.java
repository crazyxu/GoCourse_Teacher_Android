package net.gocourse.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程表的搜索返回结果
 *
 * @author loveyu
 * @date 2015/5/12 13:03
 */
public class DataCourseTableSearch implements Serializable {
	private List<DataCourseTable> list;
	private DataCurrentWeekInfo week;
	//序列化
	private static final long serialVersionUID = 121L;
	public final static String TAG="DataCourseTableSearch";

	public  DataCourseTableSearch(){}

	public DataCourseTableSearch(JSONObject json) {
		if (json != null) {
			JSONArray array = json.getJSONArray("list");
			list = new ArrayList<DataCourseTable>();
			for (int i = 0; i < array.size(); i++) {
				list.add(new DataCourseTable(array.getJSONObject(i)));
			}
			week = DataCurrentWeekInfo.parseJson(json.getJSONObject("week"));
		}

	}

	public List<DataCourseTable> getList() {
		return list;
	}

	public DataCurrentWeekInfo getWeek() {
		return week;
	}
}
