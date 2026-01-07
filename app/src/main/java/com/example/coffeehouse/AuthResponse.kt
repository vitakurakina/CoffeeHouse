package com.example.coffeehouse

data class AuthResponse(
    val message: String?,
    val userId: Int? = null,
    val login: String? = null,
    val qrToken: String? = null,
    val bonusAmount: Int? = null,
    val error: String? = null
)
