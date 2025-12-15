package com.example.coffeehouse.network

import com.example.coffeehouse.SignInRequest
import com.example.coffeehouse.AuthResponse
import com.example.coffeehouse.MenuItem
import com.example.coffeehouse.SignUpRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface GetApi {
    @GET("/drinks")
    fun getMenuDrinks(): Call<List<MenuItem>>

    @GET("/desserts")
    fun getMenuDesserts(): Call<List<MenuItem>>

    @POST("signup")
    fun signUp(@Body request: SignUpRequest): Call<AuthResponse>

    @POST("signin")
    fun signIn(@Body request: SignInRequest): Call<AuthResponse>

    @GET("/me")
    fun getMe(@Header("Authorization") token: String): Call<AuthResponse>

}
