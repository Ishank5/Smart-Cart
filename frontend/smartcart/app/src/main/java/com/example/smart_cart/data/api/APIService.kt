package com.example.smart_cart.data.api

import com.example.smart_cart.data.model.Category
import com.example.smart_cart.data.model.LoginRequest
import com.example.smart_cart.data.model.ProfileData
import com.example.smart_cart.data.model.ProfileResponse
import com.example.smart_cart.data.model.RegistrationRequest
import com.example.smart_cart.data.model.UserData
import kotlinx.coroutines.flow.Flow
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
    @GET("dashboard")
    suspend fun getCategories(): ApiResponse<List<Category>> // Returning a list of categories

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

}


// Data classes for the API response
// Generic ApiResponse class that can handle any type of response
data class ApiResponse<T>(
    val data: T? = null,  // The actual data returned by the API
    val reason: String? = null,  // Reason for failure (if any)
    val stack: String? = null    // Stack trace for errors (if any)
)

//data class Data(val categories: List<Category>, val carousels: List<Any>)

//// Repository
//class CategoryRepository {
//    private val apiService: ApiService
//
//    init {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://backend-153569340026.us-central1.run.app/api/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        apiService = retrofit.create(ApiService::class.java)
//    }
//
//    private val _categories = MutableStateFlow<List<Category>>(emptyList())
//    val categories: StateFlow<List<Category>> = _categories
//
////    suspend fun fetchCategories() {
////        withContext(Dispatchers.IO) {
////            try {
////                val response = apiService.getCategories()
////                _categories.value = response.data.categories
////            } catch (e: Exception) {
////                e.printStackTrace() // Handle error (e.g., log it)
////            }
////        }
////    }
//}
