package com.example.smart_cart.data.repository

import android.util.Log
import com.example.smart_cart.data.api.ApiResponse
import com.example.smart_cart.data.api.ApiService
import com.example.smart_cart.data.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CategoryRepository {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://backend-153569340026.us-central1.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    private val _categoriesState = MutableStateFlow<ApiResponse<List<Category>>?>(null)
    val categoriesState: StateFlow<ApiResponse<List<Category>>?> = _categoriesState

    suspend fun fetchCategories() {
        withContext(Dispatchers.IO) {
            try {
                Log.d("CategoryRepository", "Fetching categories from API")
                val response = apiService.getCategories()

                // Extract the list of categories from the nested data object
                val categories = response.data?.categories ?: emptyList()
                _categoriesState.value = ApiResponse(data = categories)

                Log.d("CategoryRepository", "Fetched ${categories.size} categories successfully")
            } catch (e: Exception) {
                Log.e("CategoryRepository", "Error fetching categories: ${e.message}", e)
                _categoriesState.value = ApiResponse(reason = e.message)
            }
        }
    }
}
