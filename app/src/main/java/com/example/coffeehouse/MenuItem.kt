package com.example.coffeehouse

data class MenuItem(
    val name: String,
    val price: String,
    val description: String,
    val info: String?=null,
    val image: String,
    val category: String
)
