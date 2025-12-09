package com.example.coffeehouse.network

import com.example.coffeehouse.MenuItem
import retrofit2.Call
import retrofit2.http.GET

interface MenuApi {
    @GET("/drinks")
    fun getMenuDrinks(): Call<List<MenuItem>>

    @GET("/desserts")
    fun getMenuDesserts(): Call<List<MenuItem>>
}
