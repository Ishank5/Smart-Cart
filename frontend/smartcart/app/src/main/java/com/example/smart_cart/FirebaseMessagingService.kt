package com.example.smart_cart

import android.R
import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private val _smartBasketId = MutableStateFlow<String?>(null)
        val smartBasketId: StateFlow<String?> = _smartBasketId
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)


        val success = remoteMessage.data["code"]
        val smartBasketId = remoteMessage.data["smartBasketId"]
        Log.d("FCM", "Message received: ${remoteMessage.data}")

        if (success == "CONNECTION_REQUEST_ACCEPTED") {
            // Store the smartBasketId and fetch in UI
            _smartBasketId.value = smartBasketId

        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
        // You can send this token to your server if needed
    }
}