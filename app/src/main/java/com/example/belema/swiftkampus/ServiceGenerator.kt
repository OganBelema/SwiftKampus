package com.example.belema.swiftkampus

import com.example.belema.swiftkampus.apiMethods.ApiMethods

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by belema on 9/13/17.
 */

object ServiceGenerator {

    private val url = "https://unibenportal.azurewebsites.net"

    private val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    var apiMethods = ServiceGenerator.createService(ApiMethods::class.java)

    fun <S> createService(
            serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }
}
