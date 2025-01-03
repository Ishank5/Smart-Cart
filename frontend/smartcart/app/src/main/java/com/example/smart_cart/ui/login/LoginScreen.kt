package com.example.smart_cart.ui.login

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.smart_cart.R
import com.example.smart_cart.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val loginState = viewModel.loginState.collectAsState().value
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.bg_image),
            contentDescription = "Background Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(start = 32.dp, top = 350.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                text = "Login",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF002B5C),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", fontSize = 12.sp) },
                placeholder = { Text("yourmail@email.com") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, end = 32.dp),
                shape = RoundedCornerShape(50),
                singleLine = true
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", fontSize = 12.sp) },
                placeholder = { Text("your password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, end = 32.dp),
                shape = RoundedCornerShape(50),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) painterResource(id = R.drawable.baseline_visibility_24)
                    else painterResource(id = R.drawable.baseline_visibility_off_24)

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = icon, contentDescription = "Toggle Password Visibility")
                    }
                }
            )


            TextButton(
                onClick = onForgotPasswordClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Forgot Password", color = Color(0xFF1976D2), fontSize = 12.sp)
            }
            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier
                    .width(300.dp)
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("Login", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Not have an account? ",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
                TextButton(onClick = onRegisterClick) {
                    Text(
                        text = "Register",
                        color = Color(0xFF1976D2),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        when (loginState) {
            is LoginState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            is LoginState.Success -> {
                val token = (loginState as LoginState.Success).token

                // Storing token in SharedPreferences
                val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                val savedToken = sharedPreferences.getString("auth_token", null)

                if (savedToken != token) {
                    val editor = sharedPreferences.edit()
                    editor.putString("auth_token", token)
                    editor.apply()

                    onLoginSuccess(token)
                }
            }
            is LoginState.Error -> {
                val errorMessage = (loginState as LoginState.Error).message
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
            LoginState.Idle -> {}
        }

    }
}


//@Composable
//@Preview(showBackground = true)
//fun PreviewLoginScreen() {
//    // Creating a mock ViewModel with mocked login state
//    val mockViewModel = object : LoginViewModel(
//        AuthRepository(firebaseAuth = FirebaseAuth.getInstance())
//    ) {
//        override val loginState: StateFlow<LoginState> = MutableStateFlow(LoginState.Idle)
//
//        override fun login(email: String, password: String) {
//            // Mock implementation for preview
//        }
//    }
//
//    // Mock functions for preview
//    val mockOnLoginSuccess: (String) -> Unit = {}
//    val mockOnRegisterClick: () -> Unit = {}
//    val mockOnForgotPasswordClick: () -> Unit = {}
//
//    // CompositionLocalProvider to handle Context and LifecycleOwner
//    CompositionLocalProvider(
//        LocalContext provides LocalContext.current // Using current LocalContext for preview
//    ) {
//        MaterialTheme {
//            LoginScreen(
//                viewModel = mockViewModel,
//                onLoginSuccess = mockOnLoginSuccess,
//                onRegisterClick = mockOnRegisterClick,
//                onForgotPasswordClick = mockOnForgotPasswordClick
//            )
//        }
//    }
//}







