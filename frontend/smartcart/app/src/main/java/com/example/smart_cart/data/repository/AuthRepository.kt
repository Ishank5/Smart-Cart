package com.example.smart_cart.data.repository


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val firebaseAuth: FirebaseAuth,
//    private val firestore: FirebaseFirestore
) {

    // Register a user
    suspend fun register(name: String, email: String, password: String): FirebaseUser? {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                // Save user details in Firestore
                //val user = UserData(uid = firebaseUser.uid, name = name, email = email)
                //firestore.collection("users").document(firebaseUser.uid).set(user).await()
            }
            firebaseAuth.currentUser
        } catch (e: Exception) {
            null
        }
    }

    // Login a user
    suspend fun login(email: String, password: String): FirebaseUser? {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            firebaseAuth.currentUser
        } catch (e: Exception) {
            null
        }
    }

    // Get user data from Firestore
//    suspend fun getUser(uid: String): UserData? {
//        return try {
//            val document = firestore.collection("users").document(uid).get().await()
//            document.toObject(UserData::class.java)
//        } catch (e: Exception) {
//            null
//        }
//    }

    // Get ID Token
    suspend fun getIdToken(): String? {
        return try {
            firebaseAuth.currentUser?.getIdToken(true)?.await()?.token
        } catch (e: Exception) {
            null
        }
    }

    fun getCurrentUserId(): String? {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        return currentUser?.uid
    }

    // Logout
    fun logout() {
        firebaseAuth.signOut()
    }
}
