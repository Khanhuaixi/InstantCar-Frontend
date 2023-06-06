package com.example.instantcar.activities.api

import com.example.instantcar.activities.utilities.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: Get by lazy {
        retrofit.create(Get::class.java)
    }
}