package com.example.smart_cart.data.model

data class RegistrationResponse(
    val data: RegistrationData? = null,
    val reason: String? = null,
    val stack: String? = null
)

data class RegistrationData(
    val id: String,
    val email: String,
    val name: String,
    val phone: String,
    val fcmToken: String?,
    val updatedAt: String,
    val createdAt: String
)
