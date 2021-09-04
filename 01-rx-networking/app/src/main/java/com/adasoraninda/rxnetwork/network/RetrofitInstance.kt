package com.adasoraninda.rxnetwork.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(JsonPlaceHolderService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: JsonPlaceHolderService by lazy {
        retrofit.create(JsonPlaceHolderService::class.java)
    }

}