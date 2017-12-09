package com.example.belema.swiftkampus.apiMethods

import com.example.belema.swiftkampus.Module
import com.example.belema.swiftkampus.MyCourses
import com.example.belema.swiftkampus.Student
import com.example.belema.swiftkampus.gson.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*

/**
 * Created by belema on 10/29/17.
 */

interface ApiMethods {

    @POST("studemtApi/checkPayment")
    fun checkPayment(@Query("studentId") studentId: String?): Call<Boolean>

    @POST("AccountApi/SignUp")
    fun checkStudentId(@Query("id") id: String): Call<StudentDetails>

    @POST("eclassroomapi/mycourses")
    fun getStudentCourses(@Query("studentId") studentId: String?): Call<ArrayList<MyCourses>>

    @POST("StudentApi/Dashboard")
    fun getDashboard(@Query("id") id: String?): Call<Dashboard>

    @POST("eclassroomapi/getmodules")
    fun getModule(@Query("courseID") courseId: String): Call<ArrayList<Module>>

    @POST("CourseRegistrationApi/GetCourses")
    fun courseRegistration(@Query("id") id: String): Call<CourseReg>

    @POST("AccountApi/RegisterStudent")
    fun registerStudent(@Body student: Student): Call<ResponseBody>

    @POST("AccountApi/Login")
    fun login(@Body student: Student): Call<UserId>

    @POST("CourseRegistrationApi/RegisterCourse")
    fun submitCourseReg(@Body courseReg: CourseReg): Call<ResponseBody>

    @POST("eclassroomapi/gettopics")
    fun getTopics(@Query("moduleId") id: String): Call<ArrayList<Topic>>

    @POST("eclassroomapi/gettopiccontent")
    fun getContent(@Query("topicId") id: String): Call<ArrayList<TopicContent>>
}
