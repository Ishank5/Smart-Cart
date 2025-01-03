package com.example.smart_cart.data.repository

import android.util.Log
import com.example.smart_cart.data.api.ApiResponse
import com.example.smart_cart.data.api.ApiService
import com.example.smart_cart.data.model.QrData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QrRepository {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://backend-153569340026.us-central1.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    private val _qrDataState = MutableStateFlow<ApiResponse<QrData>?>(null)
    val qrDataState: StateFlow<ApiResponse<QrData>?> = _qrDataState

    suspend fun getQrData(authorization: String) {
        try {
            Log.d("QrRepository", "Making API call to get QR data with token: $authorization")
            val response = apiService.getQr(authorization)
            _qrDataState.value = response

            Log.d("QrRepository", "API response: $response")
            if (response.data != null) {
                Log.d("QrRepository", "QR data: ${response.data}")
            } else {
                Log.e("QrRepository", "Failed to get QR data with reason: ${response.reason}")
            }
        } catch (e: Exception) {
            Log.e("QrRepository", "Error getting QR data: ${e.message}", e)
            _qrDataState.value = ApiResponse(reason = e.message)
        }
    }
}
