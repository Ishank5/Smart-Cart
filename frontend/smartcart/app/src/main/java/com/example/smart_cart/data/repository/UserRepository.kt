package com.example.smart_cart.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.smart_cart.data.ViewModels.GetUserState
import com.example.smart_cart.data.api.ApiService
import com.example.smart_cart.data.api.ApiResponse
import com.example.smart_cart.data.model.LoginRequest
import com.example.smart_cart.data.model.ProfileData
import com.example.smart_cart.data.model.ProfileResponse
import com.example.smart_cart.data.model.RegistrationData
import com.example.smart_cart.data.model.RegistrationRequest
import com.example.smart_cart.data.model.RegistrationResponse
import com.example.smart_cart.data.model.UserData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepository {

    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://backend-153569340026.us-central1.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    private val _registrationState = MutableStateFlow<RegistrationResponse?>(null)
    val registrationState: StateFlow<RegistrationResponse?> = _registrationState

    private val _loginState = MutableStateFlow<ApiResponse<UserData>?>(null)
    val loginState: StateFlow<ApiResponse<UserData>?> = _loginState

    private val _getUserState = MutableStateFlow<ApiResponse<ProfileData>?>(null)
    val getUserState: StateFlow<ApiResponse<ProfileData>?> = _getUserState

    // Map UserData to RegistrationData
    private fun mapUserDataToRegistrationData(userData: UserData): RegistrationData {
        Log.d("UserRepository", "Mapping UserData to RegistrationData: $userData")
        return RegistrationData(
            id = userData.id,
            email = userData.email,
            name = userData.name,
            phone = userData.phone,
            fcmToken = userData.fcmToken,
            updatedAt = userData.updatedAt,
            createdAt = userData.createdAt
        )
    }

    // Function to map ApiResponse to RegistrationResponse
    private fun mapApiResponseToRegistrationResponse(response: ApiResponse<UserData>): RegistrationResponse {
        Log.d("UserRepository", "Mapping ApiResponse to RegistrationResponse: $response")

        // If data is null, return a RegistrationResponse with reason and null data
        return RegistrationResponse(
            reason = response.reason,
            stack = response.stack,
            data = response.data?.let {
                mapUserDataToRegistrationData(it)
            } ?: RegistrationData(
                id = "",
                email = "",
                name = "",
                phone = "",
                fcmToken = null,
                updatedAt = "",
                createdAt = ""
            )  // Optionally return empty RegistrationData or an error object if backend expects data
        )
    }

    private fun mapProfileDataToProfileResponse(profileData: ProfileData): ProfileResponse {
        Log.d("UserRepository", "Mapping ProfileData to ProfileResponse: $profileData")
        return ProfileResponse(
            profileData = profileData,
            reason = null,
            stack = null
        )
    }


    suspend fun registerUser(user: RegistrationRequest, authToken: String) {

            try {
                Log.d("UserRepository", "Making API call to register user: $user")

                Log.d("UserRepository", "Making API call to register token: $authToken")
                // Making API call to register the user
                val response = apiService.registerUser(
                    registrationRequest = user,
                    authorization = authToken.toString()
                )

                Log.d("UserRepository", "API response: $response")

                // Map ApiResponse to RegistrationResponse
                val mappedResponse = mapApiResponseToRegistrationResponse(response)

                // Handle response success and failure
                if (mappedResponse.data != null) {
                    Log.d("UserRepository", "Registration successful: ${mappedResponse.data}")
                    _registrationState.value = mappedResponse // Success
                } else {
                    Log.e("UserRepository", "Registration failed with reason: ${mappedResponse.reason}")
                    _registrationState.value = mappedResponse // Error (with reason)
                }
            } catch (e: Exception) {
                Log.e("UserRepository", "Error during registration: ${e.message}")
                e.printStackTrace() // Handle the error
                _registrationState.value = RegistrationResponse(
                    reason = "Error during registration: ${e.message}",
                    stack = e.stackTraceToString()
                ) // Handle error state here
            }
    }



    suspend fun loginUser(authToken: String?) {
        try {
            Log.d("UserRepository", "Making API call to log in user with email: $authToken")

            // Validate authToken
            if (authToken?.isBlank() != false) {
                throw IllegalArgumentException("Authorization token cannot be empty")
            }

            // Creating login request
//            val loginRequest = LoginRequest(email = email, password = password)
            Log.d("UserRepository", "check Making API call to log in user with email: $authToken")
            // Making API call to log in the user
            val response = apiService.loginUser(
                authorization = authToken // Pass the authorization token
            )

            Log.d("UserRepository", "API response: $response")

            // Update state based on response
            if (response.data != null) {
                Log.d("UserRepository", "Login successful: ${response.data}")
                _loginState.value = response
            } else {
                Log.e("UserRepository", "Login failed with reason: ${response.reason}")
                _loginState.value = response
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error during login: ${e.message}", e)
            _loginState.value = ApiResponse(
                data = null,
                reason = "Error during login: ${e.message}",
                stack = e.stackTraceToString()
            )
        }
    }

    suspend fun getUserDetails(token: String, userId: String){
        try {
            // Validate token
            if (token.isBlank()) {
                throw IllegalArgumentException("Token cannot be empty")
            }

            Log.d("UserRepository", "Making API call to get user details with token: $token")
            Log.d("UserRepository", "Making API call to get user details with userId: $userId")

            // Making API call to get user details
            val response = apiService.getUser(
                userId = userId,
                authorization = token // Pass the authorization token
            )

            Log.d("UserRepository", "API response: $response")

            // Update state based on response
            if (response.data != null) {
                Log.d("UserRepository", "User details: ${response.data}")
                _getUserState.value = response
            } else {
                Log.e("UserRepository", "Failed to get user details with reason: ${response.reason}")
                _getUserState.value = response
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error getting user details: ${e.message}", e)
            _getUserState.value = ApiResponse(
                data = null,
                reason = "Error getting user details: ${e.message}",
                stack = e.stackTraceToString()
            )
        }
    }

}
