package com.example.smart_cart.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smart_cart.data.repository.AuthRepository
import com.example.smart_cart.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class LoginViewModel(private val authRepository: AuthRepository,
                          private val userRepository: UserRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    open val loginState: StateFlow<LoginState> get() = _loginState

    open fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Email and password cannot be empty")
            Log.e("LoginViewModel", "Email and password cannot be empty")
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            Log.d("LoginViewModel", "Login started")
            try {
                // First, call the login function from AuthRepository
                val firebaseUser = authRepository.login(email, password)
                if (firebaseUser != null) {
                    Log.d("LoginViewModel", "Firebase login successful")
                    // Retrieve the ID token after successful authentication
                    val token = authRepository.getIdToken()
                    if (token != null) {
                        Log.d("LoginViewModel", "Token retrieved successfully $token")
                        // Call the loginUser function from UserRepository with the retrieved token
                        userRepository.loginUser(token)
                        userRepository.loginState.collect { response ->
                            if (response?.data != null) {
                                _loginState.value = LoginState.Success(token)
                                Log.d("LoginViewModel", "Login successful")
                            } else {
                                _loginState.value = LoginState.Error(response?.reason ?: "Invalid email or password")
                                Log.e("LoginViewModel", "Login failed: ${response?.reason ?: "Invalid email or password"}")
                            }
                        }
                    } else {
                        _loginState.value = LoginState.Error("Failed to retrieve token")
                        Log.e("LoginViewModel", "Failed to retrieve token")
                    }
                } else {
                    _loginState.value = LoginState.Error("Invalid email or password")
                    Log.e("LoginViewModel", "Invalid email or password")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("An unexpected error occurred: ${e.message}")
                Log.e("LoginViewModel", "An unexpected error occurred: ${e.message}")
            }
        }
    }
}

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
}