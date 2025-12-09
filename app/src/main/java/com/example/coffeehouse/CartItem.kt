package com.example.coffeehouse

data class CartItem(
    val item: MenuItem,
    var quantity: Int = 1
)
