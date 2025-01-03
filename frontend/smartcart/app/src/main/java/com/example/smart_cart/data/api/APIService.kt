package com.example.smart_cart.data.api

import com.example.smart_cart.data.model.QrData
import com.example.smart_cart.data.model.Category
import com.example.smart_cart.data.model.ProfileData

import com.example.smart_cart.data.model.RegistrationRequest
import com.example.smart_cart.data.model.UserData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

// Retrofit API Interface
interface ApiService {





    @GET("user/login")
    suspend fun loginUser(
        @Header("Authorization") authorization: String
    ): ApiResponse<UserData>

    // Existing GET request for categories
//    @GET("dashboard")
//    suspend fun getCategories(): ApiResponse<List<Category>> // Returning a list of categories
    @GET("dashboard")
    suspend fun getCategories(): ApiResponse<CategoryResponse>


    // New POST request for user registration
    @POST("user/register")
    suspend fun registerUser(
        @Body registrationRequest: RegistrationRequest,
        @Header("Authorization") authorization: String  // The header will have the format: "Bearer <token>"
    ): ApiResponse<UserData>

    // New GET request for user details
    @GET("user/{id}")
    suspend fun getUser(
        @Path("id", encoded = true) userId: String,
        @Header("Authorization") authorization: String
    ): ApiResponse<ProfileData>

    @GET("product-variant/{productId}")
    suspend fun getProductDetails(
        @Path("productId",encoded = true) productId: String
    ): ApiResponse<ProductDetails>

    @POST("connection-request/")
    suspend fun getQr(
        @Header("Authorization") authorization: String
    ): ApiResponse<QrData>

}


// Data classes for the API response
// Generic ApiResponse class that can handle any type of response
data class ApiResponse<T>(
    val data: T? = null,  // The actual data returned by the API
    val reason: String? = null,  // Reason for failure (if any)
    val stack: String? = null    // Stack trace for errors (if any)
)

data class CategoryResponse(
    val categories: List<Category> // List of categories inside the `data` object
)



data class ProductDetailsResponse(
    val data: ProductDetails? // Nullable
)

data class ProductDetails(
    val barcode: String,
    val color: String?,
    val size: String?,
    val weight: String,
    val price: String,
    val stock: Int,
    val createdAt : String,
    val updatedAt : String,
    val productId: Int,
    val product: Product // Nullable
)

data class Product(
    val id: Int,
    val name: String,
    val description: String?,
    val manufacturer: String,
    val image: String?,
    val createdAt: String,
    val updatedAt: String,
    val categoryId: Int
)
// Repository

