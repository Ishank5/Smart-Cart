package com.example.smart_cart.ui.splash

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.smart_cart.R
import com.example.smart_cart.data.ViewModels.GetUserState
import com.example.smart_cart.data.ViewModels.UserViewModel
import com.example.smart_cart.data.repository.AuthRepository
import com.example.smart_cart.ui.signup.RegisterState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    navController: NavController, context: Context, authRepository: AuthRepository,
    userViewModel: UserViewModel,
    onTokenEmpty: () -> Unit,
    onTokenPresent: () -> Unit
) {
    val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("auth_token", null)

    val getUserState by userViewModel.getUserState.observeAsState()
    var displayError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (token.isNullOrEmpty()) {
            onTokenEmpty()
        } else {
            val freshToken = authRepository.getIdToken()
            Log.d("SplashScreen", "Old Token: $token")
            Log.d("SplashScreen", "Fresh Token: $freshToken")

            userViewModel.viewModelScope.launch {
                userViewModel.getUserDetails(authRepository)
            }
        }
    }

    LaunchedEffect(getUserState) {
        when (getUserState) {
            is GetUserState.Success -> {
                onTokenPresent()
            }
            is GetUserState.Error -> {
                displayError = true
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2382AA),
                        Color(0xFF64B5F6)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_image),
                contentDescription = "Splash Illustration",
                modifier = Modifier
                    .size(250.dp)
                    .padding(bottom = 32.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Smart Cart App",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Your smart shopping assistant",
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium
            )

            if (displayError) {
                Text(
                    text = "An error occurred. Please try again later.",
                    fontSize = 16.sp,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
        when (getUserState) {
            is GetUserState.Loading -> androidx.compose.material3.CircularProgressIndicator(
                Modifier.align(Alignment.Center)
            )
            is GetUserState.Error -> {
                val errorMessage = (getUserState as GetUserState.Error).message
                androidx.compose.material3.Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
            else -> {}
        }
    }
}
