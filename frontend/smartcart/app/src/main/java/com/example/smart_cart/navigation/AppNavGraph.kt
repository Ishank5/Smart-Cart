package com.example.smart_cart.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smart_cart.data.ViewModels.UserViewModel
import com.example.smart_cart.data.repository.AuthRepository
import com.example.smart_cart.data.repository.UserRepository
import com.example.smart_cart.ui.home.HomeScreen
import com.example.smart_cart.ui.login.LoginScreen
import com.example.smart_cart.ui.login.LoginViewModel
import com.example.smart_cart.ui.profile.MainScreen
import com.example.smart_cart.ui.signup.SignupScreen
import com.example.smart_cart.ui.signup.RegisterViewModel
import com.example.smart_cart.ui.splash.SplashScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    userViewModel: UserViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // Home Screen
        composable("home") {
            HomeScreen(
                onLoginClick = { navController.navigate("login") },
                onRegisterClick = { navController.navigate("signup") }
            )
        }

        // Login Screen
        composable("login") {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { token ->
                    // Navigate to profile Screen first
                    navController.navigate("profile")
                },
                onRegisterClick = { navController.navigate("signup") },
                onForgotPasswordClick = {
                    // Navigate to Forgot Password screen if implemented
                    navController.navigate("forgot_password")
                }
            )
        }

        // Signup Screen
        composable("signup") {
            SignupScreen(
                userViewModel = UserViewModel(),
                registerViewModel = registerViewModel,
                onAlreadyHaveAccountClick = { navController.navigate("login") }
            )
        }

        // Profile Screen
        composable("profile") {
            MainScreen(
                onLogoutClick = {
                    // Navigate back to login on logout
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                userViewModel= userViewModel
            )
        }

        // Splash Screen

        composable("splash") {
            SplashScreen(
                navController = navController,
                context = LocalContext.current,
                AuthRepository(firebaseAuth = FirebaseAuth.getInstance()),
                userViewModel = userViewModel,
                onTokenEmpty = {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onTokenPresent = {
                    navController.navigate("profile") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // Placeholder for Forgot Password Screen (if needed)
        composable("forgot_password") {
            // Add your ForgotPasswordScreen here
            // ForgotPasswordScreen()
        }
    }
}
