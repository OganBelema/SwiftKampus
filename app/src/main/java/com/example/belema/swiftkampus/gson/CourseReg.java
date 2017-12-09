
package com.example.belema.swiftkampus.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CourseReg {

    @SerializedName("studentId")
    @Expose
    private String studentId;
    @SerializedName("levelId")
    @Expose
    private Integer levelId;
    @SerializedName("programmeId")
    @Expose
    private Integer programmeId;
    @SerializedName("departmentId")
    @Expose
    private Object departmentId;
    @SerializedName("semesterId")
    @Expose
    private Integer semesterId;
    @SerializedName("sessionId")
    @Expose
    private Integer sessionId;
    @SerializedName("courseId")
    @Expose
    private Object courseId;
    @SerializedName("carryOverCoursesId")
    @Expose
    private List<Object> carryOverCoursesId = null;
    @SerializedName("availableCredit")
    @Expose
    private Integer availableCredit;
    @SerializedName("student")
    @Expose
    private Object student;
    @SerializedName("courses")
    @Expose
    private ArrayList<Course> courses = null;
    @SerializedName("carryOverCourses")
    @Expose
    private ArrayList<Course> carryOverCourses = null;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public Integer getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(Integer programmeId) {
        this.programmeId = programmeId;
    }

    public Object getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Object departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Object getCourseId() {
        return courseId;
    }

    public void setCourseId(Object courseId) {
        this.courseId = courseId;
    }

    public List<Object> getCarryOverCoursesId() {
        return carryOverCoursesId;
    }

    public void setCarryOverCoursesId(List<Object> carryOverCoursesId) {
        this.carryOverCoursesId = carryOverCoursesId;
    }

    public Integer getAvailableCredit() {
        return availableCredit;
    }

    public void setAvailableCredit(Integer availableCredit) {
        this.availableCredit = availableCredit;
    }

    public Object getStudent() {
        return student;
    }

    public void setStudent(Object student) {
        this.student = student;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public ArrayList<Course> getCarryOverCourses() {
        return carryOverCourses;
    }

    public void setCarryOverCourses(ArrayList<Course> carryOverCourses) {
        this.carryOverCourses = carryOverCourses;
    }

}
