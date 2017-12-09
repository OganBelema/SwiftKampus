package com.example.belema.swiftkampus.gson

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserId {

    @SerializedName("studentId")
    @Expose
    var studentId: String? = null
}
