package com.example.smart_cart.data.model

data class RegistrationRequest(
    val name: String,
    val phone: String,
    val fcmToken: String? = null // Optional
)
