package net.gocourse.model;

import java.io.Serializable;

/**
 * 课程表查询结果数据
 *
 * @author loveyu
 * @date 2015/5/6 10:49
 */
public class TbCourseTableSearch implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int scheduleID;
	public int courseTableID;
	public int teacherID;
	public String teacherName;
	public int deptID;
	public String deptName;
	public String deptNickName;
	public int enrolYear;
	public int courseID;
	public String courseName;
	public int openYear;
	public int openTerm;
	public int fromWeek;
	public int endWeek;
	public int status = -1;// 0为有效值

	public int getScheduleID() {
		return scheduleID;
	}

	public void setScheduleID(int scheduleID) {
		this.scheduleID = scheduleID;
	}

	public int getCourseTableID() {
		return courseTableID;
	}

	public void setCourseTableID(int courseTableID) {
		this.courseTableID = courseTableID;
	}

	public int getTeacherID() {
		return teacherID;
	}

	public void setTeacherID(int teacherID) {
		this.teacherID = teacherID;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public int getDeptID() {
		return deptID;
	}

	public void setDeptID(int deptID) {
		this.deptID = deptID;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptNickName() {
		return deptNickName;
	}

	public void setDeptNickName(String deptNickName) {
		this.deptNickName = deptNickName;
	}

	public int getEnrolYear() {
		return enrolYear;
	}

	public void setEnrolYear(int enrolYear) {
		this.enrolYear = enrolYear;
	}

	public int getCourseID() {
		return courseID;
	}

	public void setCourseID(int courseID) {
		this.courseID = courseID;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getOpenYear() {
		return openYear;
	}

	public void setOpenYear(int openYear) {
		this.openYear = openYear;
	}

	public int getOpenTerm() {
		return openTerm;
	}

	public void setOpenTerm(int openTerm) {
		this.openTerm = openTerm;
	}

	public int getFromWeek() {
		return fromWeek;
	}

	public void setFromWeek(int fromWeek) {
		this.fromWeek = fromWeek;
	}

	public int getEndWeek() {
		return endWeek;
	}

	public void setEndWeek(int endWeek) {
		this.endWeek = endWeek;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
