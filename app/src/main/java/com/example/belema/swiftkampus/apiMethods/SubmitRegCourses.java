package com.example.belema.swiftkampus.apiMethods;

import com.example.belema.swiftkampus.gson.CourseReg;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by belema on 9/29/17.
 */

public interface SubmitRegCourses {
    @POST("CourseRegistrationApi/RegisterCourse")
    Call<ResponseBody> submitCourseReg(@Body CourseReg courseReg);
}
