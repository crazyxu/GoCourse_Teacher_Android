package net.gocourse.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 查询课表ID及对应的班级信息
 *
 * @author loveyu
 * @date 2015/5/6 14:54
 */
public class CourseTableIdWithClass implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int classID;
	public String className;
	public int courseTableID;

	public static CourseTableIdWithClass parseJson(JSONObject jsb) {
		return JSON.parseObject(jsb.toJSONString(),
				CourseTableIdWithClass.class);
	}

	public int getClassID() {
		return classID;
	}

	public void setClassID(int classID) {
		this.classID = classID;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getCourseTableID() {
		return courseTableID;
	}

	public void setCourseTableID(int courseTableID) {
		this.courseTableID = courseTableID;
	}

}
