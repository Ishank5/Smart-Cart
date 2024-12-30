package com.example.smart_cart.data.ViewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smart_cart.data.model.RegistrationRequest
import com.example.smart_cart.data.model.RegistrationResponse
import com.example.smart_cart.data.repository.UserRepository
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.example.smart_cart.data.api.ApiResponse
import com.example.smart_cart.data.model.ProfileData
import com.example.smart_cart.data.model.ProfileResponse
import com.example.smart_cart.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// State class for registration to represent different states (Loading, Success, Error)
sealed class UserRegistrationState {
    object Loading : UserRegistrationState()
    data class Success(val response: RegistrationResponse) : UserRegistrationState()
    data class Error(val message: String) : UserRegistrationState()
}

sealed class GetUserState {
    object Loading : GetUserState()
    data class Success(val response: ApiResponse<ProfileData>) : GetUserState()
    data class Error(val message: String) : GetUserState()
}


class UserViewModel() : ViewModel() {
    private val userRepository = UserRepository()


    private val _userRegistrationState = MutableLiveData<UserRegistrationState>()
    val userRegistrationState: LiveData<UserRegistrationState> get() = _userRegistrationState

    private val _getUserState = MutableLiveData<GetUserState>()
    val getUserState: LiveData<GetUserState> get() = _getUserState

    suspend fun registerUser(user: RegistrationRequest, authRepository: AuthRepository) {
        val token = authRepository.getIdToken()

        // Log to check if the token is null or empty
        Log.d("UserViewModel", "ID Token: $token")

        // Check user details for debugging
        Log.d("UserViewModel", "User details: ${user.toString()}")

        _userRegistrationState.postValue(UserRegistrationState.Loading) // Set loading state

        viewModelScope.launch {
            try {
                Log.d("UserViewModel", "Calling registerUser in UserRepository")

                // Log before API call
                Log.d("UserViewModel", "Registering user with token: $token")

                userRepository.registerUser(
                    user = user,
                    authToken = token.toString(),
                )

                userRepository.registrationState.collect { response ->
                    // Log the response to see the registration result
                    Log.d("UserViewModel", "Registration Response: $response")

                    if (response != null) {
                        if (response.reason != null) {
                            // If there's a reason (error), log and handle the error message
                            Log.e("UserViewModel", "Registration failed: ${response.reason}")
                            _userRegistrationState.postValue(UserRegistrationState.Error(response.reason))
                        } else {
                            // If registration is successful, log the success
                            Log.d("UserViewModel", "Registration successful: ${response.data}")
                            _userRegistrationState.postValue(UserRegistrationState.Success(response))
                        }
                    } else {
                        // Log when the response is null to debug the issue
                        Log.e("UserViewModel", "Received null response from registration state")
                        _userRegistrationState.postValue(UserRegistrationState.Error("Unexpected error occurred"))
                    }
                }
            } catch (e: Exception) {
                // Log the error if the registration fails due to any exception
                Log.e("UserViewModel", "Error during registration: ${e.message}", e)
                _userRegistrationState.postValue(UserRegistrationState.Error("Error during registration: ${e.message}"))
            }
        }
    }



    suspend fun getUserDetails(authRepository: AuthRepository) {
        val freshToken = authRepository.getIdToken()
        val userId=authRepository.getCurrentUserId()

        Log.d("UserViewModel", "User ID: $userId")
        Log.d("UserViewModel", "Fresh Token: $freshToken")

        _getUserState.postValue(GetUserState.Loading)

        viewModelScope.launch {
            try {
                Log.d("UserViewModel", "Calling getUserDetails in UserRepository")

                userRepository.getUserDetails(
                    userId = userId.toString(),
                    token = freshToken.toString()
                )

                userRepository.getUserState.collect { response ->
                    Log.d("UserViewModel", "User Response: $response")

                    if (response != null) {
                        if (response.reason != null) {
                            Log.e("UserViewModel", "User details failed: ${response.reason}")
                            _getUserState.postValue(GetUserState.Error(response.reason))
                        } else {
                            Log.d("UserViewModel", "User details successful: ${response.data}")
                            _getUserState.postValue(GetUserState.Success(response))
                        }
                    } else {
                        Log.e("UserViewModel", "Received null response from user state")
                        _getUserState.postValue(GetUserState.Error("Unexpected error occurred"))
                    }
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error during user details: ${e.message}", e)
                _getUserState.postValue(GetUserState.Error("Error during user details: ${e.message}"))
            }
        }
    }
    // create a function here that stores the user details in state to be used elsewhere in the app


}