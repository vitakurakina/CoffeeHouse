package com.example.coffeehouse

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "orders")
@TypeConverters(CartItemListConverter::class)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cartItems: List<CartItem>
)
