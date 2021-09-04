package com.adasoraninda.rxnetwork.network

import com.adasoraninda.rxnetwork.model.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface JsonPlaceHolderService {

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }

    @GET("posts/{id}")
    fun getPostsById(
        @Path("id") id: Int
    ): Call<Post?>

}