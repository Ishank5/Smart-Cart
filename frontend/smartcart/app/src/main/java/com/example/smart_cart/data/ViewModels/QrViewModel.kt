package com.example.smart_cart.data.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smart_cart.data.api.ApiResponse
import com.example.smart_cart.data.model.QrData
import com.example.smart_cart.data.repository.AuthRepository
import com.example.smart_cart.data.repository.QrRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

sealed class QrState {
    object Loading : QrState()
    data class Success(val response: ApiResponse<QrData>) : QrState()
    data class Error(val message: String) : QrState()
}

class QrViewModel : ViewModel() {

    private val qrRepository = QrRepository()

    private val _qrState = MutableLiveData<QrState>()
    val qrState: LiveData<QrState> get() = _qrState

    // Store id in LiveData
    private val _qrId = MutableLiveData<String>()
    val qrId: LiveData<String> get() = _qrId

    fun getQrDetails(authRepository: AuthRepository) {
        viewModelScope.launch {
            try {
                _qrState.postValue(QrState.Loading)
                val freshToken = authRepository.getIdToken()
                Log.d("QrViewModel", "Fresh Token: $freshToken")

                // Fetch QR data from the repository
                qrRepository.getQrData(freshToken.toString())

                // Observe qrDataState and update the state based on the response
                qrRepository.qrDataState.collect { qrResponse ->
                    Log.d("QrViewModel", "QR Response: $qrResponse")
                    if (qrResponse?.data != null) {
                        // Update the QR id in LiveData
                        _qrId.postValue(qrResponse.data.id)

                        _qrState.postValue(QrState.Success(qrResponse))
                        Log.d("QrViewModel", "QR data: ${qrResponse.data}")
                    } else {
                        _qrState.postValue(QrState.Error(qrResponse?.reason ?: "Failed to get QR data"))
                        Log.e("QrViewModel", "Failed to get QR data with reason: ${qrResponse?.reason}")
                    }
                }

            } catch (e: Exception) {
                _qrState.postValue(QrState.Error(e.message ?: "An unexpected error occurred"))
                Log.e("QrViewModel", "An unexpected error occurred: ${e.message}")
            }
        }
    }
}



