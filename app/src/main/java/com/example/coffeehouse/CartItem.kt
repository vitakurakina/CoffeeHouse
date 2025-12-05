package com.example.coffeehouse

data class CartItem(
    val item: MenuDrinksItem,
    var quantity: Int = 1
)
