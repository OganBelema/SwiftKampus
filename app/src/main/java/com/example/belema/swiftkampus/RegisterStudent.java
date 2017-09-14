package com.example.belema.swiftkampus;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by belema on 9/13/17.
 */

public interface RegisterStudent {
    @POST("AccountApi/RegisterStudent")
    Call<ResponseBody> registerStudent(@Body Student student);
}
