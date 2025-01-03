package com.example.smart_cart.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.smart_cart.data.api.ApiService
import com.example.smart_cart.data.api.ProductDetails
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItemsRepository() {

    private val apiService: ApiService


    init {
        Log.d("ItemsRepository", "Initializing repository and Retrofit")
        val retrofit = Retrofit.Builder()
            .baseUrl("https://backend-153569340026.us-central1.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    private val _itemsState = MutableStateFlow<List<ProductDetails>>(emptyList())
    val itemsState: StateFlow<List<ProductDetails>> = _itemsState

    fun observeItems(id: Int) {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("SmartBaskets/$id/items")
        Log.d("ItemsRepository", "Starting to observe items in Firebase")
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val productId = snapshot.key // productId is a String
                Log.d("ItemsRepository", "New productId added: $productId")

                if (productId != null) {
                    Log.d("ItemsRepository", "Fetching product details for productId: $productId")
                    fetchProductDetails(productId) // Only fetch product details using productId
                } else {
                    Log.e("ItemsRepository", "ProductId is null")
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val productId = snapshot.key // productId is a String
                Log.d("ItemsRepository", "Child changed: $productId")

                if (productId != null) {
                    Log.d("ItemsRepository", "Fetching product details for productId: $productId")
                    fetchProductDetails(productId) // Only fetch product details using productId
                } else {
                    Log.e("ItemsRepository", "ProductId is null")
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Log.d("ItemsRepository", "Child removed: ${snapshot.key}")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("ItemsRepository", "Child moved: ${snapshot.key}")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ItemsRepository", "Database error: ${error.message}")
            }
        })
    }

    private fun fetchProductDetails(productId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("ItemsRepository", "Fetching product details for productId: $productId")
                // Fetch product details from API
                val response = apiService.getProductDetails(productId)
                Log.d("ItemsRepository", "API response received for productId: $productId")
                Log.d("ItemsRepository", "API response: $response")

                val productDetails = response.data?.let {
                    Log.d("ItemsRepository", "Parsing product details for productId: $productId")
                    ProductDetails(
                        barcode = it.barcode,
                        color = it.color,
                        price = it.price,
                        weight = it.weight,
                        size = it.size,
                        stock = it.stock,
                        productId = it.productId,
                        product = it.product ,
                        createdAt =it.createdAt,
                        updatedAt = it.updatedAt // Throw an error if product is null
                    )
                }

                // Update the state with the new item
                if (productDetails != null) {
                    Log.d("ItemsRepository", "Updating state with new product details for productId: $productId")
                    _itemsState.update { currentList ->
                        currentList + productDetails
                    }
                } else {
                    Log.e("ItemsRepository", "Product details are null for productId: $productId")
                }
            } catch (e: Exception) {
                Log.e("ItemsRepository", "Error fetching product details for productId: $productId", e)
            }
        }
    }
}