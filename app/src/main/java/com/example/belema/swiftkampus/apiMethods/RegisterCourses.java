package com.example.belema.swiftkampus.apiMethods;

import com.example.belema.swiftkampus.gson.CourseReg;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by belema on 9/28/17.
 */

public interface RegisterCourses {
    @POST("CourseRegistrationApi/GetCourses")
    Call<CourseReg> courseRegistration(@Query("id") String id);
}
