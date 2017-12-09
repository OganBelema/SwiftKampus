package com.example.belema.swiftkampus.gson

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Dashboard {

    @SerializedName("studentId")
    @Expose
    var studentId: String? = null
    @SerializedName("fullName")
    @Expose
    var fullName: String? = null
    @SerializedName("levelName")
    @Expose
    var levelName: String? = null
    @SerializedName("programmeName")
    @Expose
    var programmeName: String? = null
    @SerializedName("departmentName")
    @Expose
    var departmentName: String? = null
    @SerializedName("semesterName")
    @Expose
    var semesterName: String? = null
    @SerializedName("sessionName")
    @Expose
    var sessionName: String? = null
    @SerializedName("facultyName")
    @Expose
    var facultyName: String? = null
    @SerializedName("noOfRegCourses")
    @Expose
    var noOfRegCourses: Int? = null
    @SerializedName("passport")
    @Expose
    var passport: String? = null
    @SerializedName("schoolFees")
    @Expose
    var schoolFees: Int? = null

}
