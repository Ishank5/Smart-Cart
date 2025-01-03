package com.example.smart_cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData

class SmartBasketViewModel : ViewModel() {
    val smartBasketId = MyFirebaseMessagingService.smartBasketId.asLiveData()

}