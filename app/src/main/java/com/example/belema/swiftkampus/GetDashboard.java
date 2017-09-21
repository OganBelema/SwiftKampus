package com.example.belema.swiftkampus;

import com.example.belema.swiftkampus.Gson.Dashboard;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by belema on 9/21/17.
 */

public interface GetDashboard {
    @POST("StudentApi/Dashboard")
    Call<Dashboard> getDashboard(@Query("id") String id);
}
