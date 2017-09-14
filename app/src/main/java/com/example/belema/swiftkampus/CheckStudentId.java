package com.example.belema.swiftkampus;

import com.example.belema.swiftkampus.Gson.StudentDetails;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by belema on 9/13/17.
 */

public interface CheckStudentId {
    @POST("AccountApi/SignUp")
    Call<StudentDetails> checkStudentId(@Query("id") String id);
}
