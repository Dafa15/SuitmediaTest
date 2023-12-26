package com.dafa.suitmediatest.api

import com.dafa.suitmediatest.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("users")
    fun getUserData(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): Call<UserResponse>
}