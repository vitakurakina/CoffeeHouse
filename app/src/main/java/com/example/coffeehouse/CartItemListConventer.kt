package com.example.coffeehouse

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartItemListConverter {
    @TypeConverter
    fun fromCartItemList(list: List<CartItem>): String = Gson().toJson(list)

    @TypeConverter
    fun toCartItemList(data: String): List<CartItem> {
        val type = object : TypeToken<List<CartItem>>() {}.type
        return Gson().fromJson(data, type)
    }
}
