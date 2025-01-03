package com.example.smart_cart.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smart_cart.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> get() = _registerState

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        if (password.isBlank() || email.isBlank() || name.isBlank()) {
            _registerState.value = RegisterState.Error("All fields are required")
            return
        }

        if (password != confirmPassword) {
            _registerState.value = RegisterState.Error("Passwords do not match")
            return
        }

        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            val user = authRepository.register(name, email, password)
            if (user != null) {
                _registerState.value = RegisterState.Success(user)
            } else {
                _registerState.value = RegisterState.Error("Firebase Registration failed")
            }
        }
    }
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val user: Any?) : RegisterState()
    data class Error(val message: String) : RegisterState()
}
