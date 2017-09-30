
package com.example.belema.swiftkampus.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dashboard {

    @SerializedName("studentId")
    @Expose
    private String studentId;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("levelName")
    @Expose
    private Object levelName;
    @SerializedName("programmeName")
    @Expose
    private String programmeName;
    @SerializedName("departmentName")
    @Expose
    private String departmentName;
    @SerializedName("semesterName")
    @Expose
    private String semesterName;
    @SerializedName("sessionName")
    @Expose
    private String sessionName;
    @SerializedName("facultyName")
    @Expose
    private String facultyName;
    @SerializedName("noOfRegCourses")
    @Expose
    private Integer noOfRegCourses;
    @SerializedName("passport")
    @Expose
    private String passport;
    @SerializedName("schoolFees")
    @Expose
    private Integer schoolFees;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Object getLevelName() {
        return levelName;
    }

    public void setLevelName(Object levelName) {
        this.levelName = levelName;
    }

    public String getProgrammeName() {
        return programmeName;
    }

    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public Integer getNoOfRegCourses() {
        return noOfRegCourses;
    }

    public void setNoOfRegCourses(Integer noOfRegCourses) {
        this.noOfRegCourses = noOfRegCourses;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public Integer getSchoolFees() {
        return schoolFees;
    }

    public void setSchoolFees(Integer schoolFees) {
        this.schoolFees = schoolFees;
    }

}
