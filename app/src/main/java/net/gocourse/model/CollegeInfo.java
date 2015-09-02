package net.gocourse.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Created by loveyu on 2015/4/27 21:13. 一个用于查询教师所在学院的信息
 */
public class CollegeInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int collegeID;
	private String collegeName;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getCollegeID() {
		return collegeID;
	}

	public void setCollegeID(int collegeID) {
		this.collegeID = collegeID;
	}

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public int getUniID() {
		return uniID;
	}

	public void setUniID(int uniID) {
		this.uniID = uniID;
	}

	public String getUniName() {
		return uniName;
	}

	public void setUniName(String uniName) {
		this.uniName = uniName;
	}

	public String getUniNickname() {
		return uniNickname;
	}

	public void setUniNickname(String uniNickname) {
		this.uniNickname = uniNickname;
	}

	private int uniID;
	private String uniName;
	private String uniNickname;

	public static CollegeInfo parseJson(JSONObject jsb) {
		return JSON.parseObject(jsb.toJSONString(), CollegeInfo.class);
	}
}
