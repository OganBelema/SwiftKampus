package com.example.belema.swiftkampus.gson

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Course {

    @SerializedName("courseId")
    @Expose
    var courseId: Int = 0
    @SerializedName("courseName")
    @Expose
    var courseName: String = ""
    @SerializedName("credit")
    @Expose
    var credit: Int = 0

}
