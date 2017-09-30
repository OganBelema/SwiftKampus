package com.example.belema.swiftkampus.apiMethods;

import com.example.belema.swiftkampus.Student;
import com.example.belema.swiftkampus.gson.UserId;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by belema on 9/14/17.
 */

public interface StudentLogin {
    @POST("AccountApi/Login")
    Call<UserId> login(@Body Student student);
}
