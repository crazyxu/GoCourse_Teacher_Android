package net.gocourse.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询的课程表数据
 *
 * @author loveyu
 * @date 2015/5/15 0:37
 */
public class DataCourseTable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TbCourseTableSearch course;
	public int[] classes_id;
	public List<CourseTableIdWithClass> classes_info;
	public List<TbCourseLocation> locations;

	public DataCourseTable(JSONObject obj) {
		course = obj.getObject("course", TbCourseTableSearch.class);

		JSONArray arr = obj.getJSONArray("classes_id");
		if (arr != null) {
			classes_id = new int[arr.size()];
			for (int i = 0; i < classes_id.length; i++) {
				classes_id[i] = arr.getIntValue(i);
			}
		}

		JSONArray array1 = obj.getJSONArray("classes_info");
		classes_info = new ArrayList<CourseTableIdWithClass>();
		for (int i = 0; i < array1.size(); i++) {
			classes_info.add(CourseTableIdWithClass.parseJson(array1
					.getJSONObject(i)));
		}

		JSONArray array2 = obj.getJSONArray("locations");
		locations = new ArrayList<TbCourseLocation>();
		for (int i = 0; i < array2.size(); i++) {
			locations.add(TbCourseLocation.parseJson(array2.getJSONObject(i)));
		}

	}

	public TbCourseTableSearch getCourse() {
		return course;
	}

	public void setCourse(TbCourseTableSearch course) {
		this.course = course;
	}

	public int[] getClasses_id() {
		return classes_id;
	}

	public void setClasses_id(int[] classes_id) {
		this.classes_id = classes_id;
	}

	public List<CourseTableIdWithClass> getClasses_info() {
		return classes_info;
	}

	public void setClasses_info(List<CourseTableIdWithClass> classes_info) {
		this.classes_info = classes_info;
	}

	public List<TbCourseLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<TbCourseLocation> locations) {
		this.locations = locations;
	}

}
