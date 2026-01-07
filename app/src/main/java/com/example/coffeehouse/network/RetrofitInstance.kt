package com.example.coffeehouse.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://soiarnami.pythonanywhere.com/"
//    private const val BASE_URL = "http://192.168.1.58:5000/"
    val api: GetApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GetApi::class.java)
    }
}
