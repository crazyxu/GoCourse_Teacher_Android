package net.gocourse.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 上课地点对象
 *
 * @author loveyu
 * @date 2015/5/3 11:51
 */
public class TbCourseLocation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int clID;
	public int courseTableID;
	public String location;
	public String week;
	public int day;
	public int slot;
	public String notice;

	public static TbCourseLocation parseJson(JSONObject jsb) {
		return JSON.parseObject(jsb.toJSONString(), TbCourseLocation.class);
	}

	public int getClID() {
		return clID;
	}

	public void setClID(int clID) {
		this.clID = clID;
	}

	public int getCourseTableID() {
		return courseTableID;
	}

	public void setCourseTableID(int courseTableID) {
		this.courseTableID = courseTableID;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

}
