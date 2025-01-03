package com.example.smart_cart.data.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smart_cart.data.api.ProductDetails
import com.example.smart_cart.data.repository.ItemsRepository
import kotlinx.coroutines.launch

sealed class ItemsState {
    object Loading : ItemsState()
    data class Success(val items: List<ProductDetails>) : ItemsState()
    data class Error(val message: String) : ItemsState()
}

class ItemsViewModel : ViewModel() {

    //make a function here to update value of smartBasketId i will call this function from UI

    private val repository = ItemsRepository()

    private val _itemsState = MutableLiveData<ItemsState>()
    val itemsState: LiveData<ItemsState> get() = _itemsState
    private var hasObservedItems = false



     fun observeItems(id: Int) {
        if (hasObservedItems) {
            return
        }
        hasObservedItems = true
        viewModelScope.launch {
            Log.d("ItemsViewModel", "Observing items...")
            _itemsState.postValue(ItemsState.Loading)

            try {
                repository.observeItems(id)
                Log.d("ItemsViewModel", "Started observing items from repository")

                repository.itemsState.collect { items ->
                    Log.d("ItemsViewModel", "Items collected: $items")
                    _itemsState.postValue(ItemsState.Success(items))
                }
            } catch (e: Exception) {
                Log.e("ItemsViewModel", "Error observing items: ${e.message}", e)
                _itemsState.postValue(ItemsState.Error(e.message ?: "An unexpected error occurred"))
            }
        }
    }
}