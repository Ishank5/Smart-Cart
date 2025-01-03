package com.example.smart_cart.data.model

data class Category(
    val id: Int,
    val name: String,
    val description: String,
    val image: String? // Assuming the image will be a URL or null
)
