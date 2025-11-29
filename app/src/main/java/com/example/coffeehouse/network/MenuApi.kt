package com.example.coffeehouse.network

import com.example.coffeehouse.MenuDrinksItem
import retrofit2.Call
import retrofit2.http.GET

interface MenuApi {
    @GET("/menu")
    fun getMenu(): Call<List<MenuDrinksItem>>

}
