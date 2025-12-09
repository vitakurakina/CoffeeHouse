package com.example.coffeehouse

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [OrderEntity::class], version = 1)
@TypeConverters(CartItemListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
}
