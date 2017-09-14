package com.example.belema.swiftkampus;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by belema on 9/14/17.
 */

public interface StudentLogin {
    @POST("AccountApi/Login")
    Call<ResponseBody> login(@Body Student student);
}
