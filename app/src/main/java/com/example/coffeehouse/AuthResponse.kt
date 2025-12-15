package com.example.coffeehouse

data class AuthResponse(
    val message: String?,
    val user_id: Int? = null,
    val login: String? = null,
    val qr_token: String? = null,
    val bonus_amount: String? = null,
    val error: String? = null
)
