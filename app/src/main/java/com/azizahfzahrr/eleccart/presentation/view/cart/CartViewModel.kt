package com.azizahfzahrr.eleccart.presentation.view.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azizahfzahrr.eleccart.data.local.entity.CartItem
import com.azizahfzahrr.eleccart.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository: CartRepository) : ViewModel() {

    val allCartItems = cartRepository.getAllCartItems()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addItemToCart(cartItem: CartItem) {
        viewModelScope.launch {
            cartRepository.addSingleCartItem(cartItem)
        }
    }

    fun updateItemInCart(cartItem: CartItem) {
        viewModelScope.launch {
            cartRepository.updateCartItem(cartItem)
        }
    }

    fun deleteItemFromCart(cartItem: CartItem) {
        viewModelScope.launch {
            cartRepository.deleteCartItem(cartItem)
        }
    }

    fun deleteAllItemsFromCart() {
        viewModelScope.launch {
            cartRepository.deleteAllCartItems()
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.deleteAllCartItems()
        }
    }
}