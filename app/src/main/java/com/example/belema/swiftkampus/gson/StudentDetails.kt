package com.example.belema.swiftkampus.gson

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StudentDetails {

    @SerializedName("userId")
    @Expose
    var userId: String? = null
    @SerializedName("firstName")
    @Expose
    var firstName: String? = null
    @SerializedName("lastName")
    @Expose
    var lastName: String? = null
    @SerializedName("department")
    @Expose
    var department: String? = null

}
