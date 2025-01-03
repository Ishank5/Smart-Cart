package com.example.smart_cart.ui.signup

import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.smart_cart.R
import com.example.smart_cart.data.ViewModels.UserRegistrationState
import com.example.smart_cart.data.ViewModels.UserViewModel
import com.example.smart_cart.data.model.RegistrationRequest
import com.example.smart_cart.data.model.RegistrationResponse
import com.example.smart_cart.data.repository.AuthRepository
import kotlinx.coroutines.launch

@Composable
fun SignupScreen(
    registerViewModel: RegisterViewModel,
    userViewModel: UserViewModel,
    onAlreadyHaveAccountClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) } // For showing the success dialog

    val registerState by registerViewModel.registerState.collectAsState()
    val userRegistrationState by userViewModel.userRegistrationState.observeAsState()

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
                .padding(start = 32.dp, top = 250.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Signup",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF002B5C),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name", fontSize = 12.sp) },
                placeholder = { Text("Your name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, end = 32.dp),
                shape = RoundedCornerShape(50),
                singleLine = true
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
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone", fontSize = 12.sp) },
                placeholder = { Text("Your phone number") },
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
                placeholder = { Text("Your password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, end = 32.dp),
                shape = RoundedCornerShape(50),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon =
                        if (passwordVisible) painterResource(id = R.drawable.baseline_visibility_24)
                        else painterResource(id = R.drawable.baseline_visibility_off_24)

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = icon, contentDescription = "Toggle Password Visibility")
                    }
                }
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password", fontSize = 12.sp) },
                placeholder = { Text("Re-enter your password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, end = 32.dp),
                shape = RoundedCornerShape(50),
                singleLine = true,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon =
                        if (confirmPasswordVisible) painterResource(id = R.drawable.baseline_visibility_24)
                        else painterResource(id = R.drawable.baseline_visibility_off_24)

                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            painter = icon,
                            contentDescription = "Toggle Confirm Password Visibility"
                        )
                    }
                }
            )

            Button(
                onClick = {
                    // Register with Firebase first
                    registerViewModel.register(
                        email = email,
                        password = password,
                        name = name,
                        confirmPassword = confirmPassword
                    )

                    // Get the Firebase ID Token and make the API call
                    val auth = FirebaseAuth.getInstance()

                    auth.currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            val fcmToken: String? = "fcmtoken"

                            // Proceed with the registration API call after Firebase auth success
                            val registrationRequest = RegistrationRequest(
                                name = name,
                                phone = phone.toString(),
                                fcmToken = fcmToken // Pass the FCM token here
                            )

                            // Launch a coroutine to call registerUser
                            userViewModel.viewModelScope.launch {
                                userViewModel.registerUser(
                                    user = registrationRequest,
                                    authRepository = AuthRepository(
                                        firebaseAuth = FirebaseAuth.getInstance()
                                    )
                                )
                            }
                        } else {
                            // Handle token retrieval failure
                            println("Error getting Firebase token: ${task.exception?.message}")
                        }
                    }
                    },
                modifier = Modifier
                    .width(300.dp)
                    .padding(vertical = 10.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))) {
                Text(
                    "Register",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
                TextButton(onClick = onAlreadyHaveAccountClick) {
                    Text(
                        text = "Login",
                        color = Color(0xFF1976D2),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Handle registration state changes
        when (registerState) {
            is RegisterState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            is RegisterState.Success -> {
                //showDialog = true
            }
            is RegisterState.Error -> {
                val errorMessage = (registerState as RegisterState.Error).message
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
            else -> {}
        }

        when (userRegistrationState) {
            is UserRegistrationState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
            is UserRegistrationState.Success -> {
                // Handle success for API registration if needed
                showDialog = true
            }
            is UserRegistrationState.Error -> {
                val errorMessage = (userRegistrationState as UserRegistrationState.Error).message
                Text(
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

    // Show Success Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Registration Successful") },
            text = { Text("You have successfully registered. Now you can log in.") },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    onAlreadyHaveAccountClick() // Navigate to login screen
                }) {
                    Text("Login Now")
                }
            }
        )
    }
}
