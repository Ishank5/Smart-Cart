package com.example.smart_cart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.smart_cart.data.ViewModels.QrViewModel
import com.example.smart_cart.data.ViewModels.UserViewModel
import com.example.smart_cart.data.repository.AuthRepository
import com.example.smart_cart.data.repository.UserRepository
import com.example.smart_cart.data.ViewModels.CategoryViewModel
import com.example.smart_cart.data.ViewModels.ItemsViewModel
import com.example.smart_cart.ui.login.LoginViewModel
import com.example.smart_cart.ui.signup.RegisterViewModel
import com.example.smart_cart.navigation.AppNavGraph

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            // Initialize Firebase instances
            val firebaseAuth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()

            val authRepository = AuthRepository(firebaseAuth)
            val userRepository = UserRepository()


            val loginViewModel = LoginViewModel(authRepository, userRepository)
            val registerViewModel = RegisterViewModel(authRepository)

            val userViewModel = UserViewModel()
            val qrViewModel = QrViewModel()
            val smartBasketViewModel = SmartBasketViewModel()
            val itemsViewModel = ItemsViewModel()
            // Apply Theme and Use AppNavGraph for navigation
            val categoryViewModel = CategoryViewModel()
            LaunchedEffect(Unit) {
                categoryViewModel.fetchCategories()
            }
                AppNavGraph(
                    loginViewModel = loginViewModel,
                    registerViewModel = registerViewModel,
                    userViewModel = userViewModel,
                    qrViewModel = qrViewModel,
                    smartBasketViewModel = smartBasketViewModel,
                    categoryViewModel = categoryViewModel,
                    itemsViewModel = itemsViewModel
                )

        }
    }
}
