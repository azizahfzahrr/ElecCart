package com.azizahfzahrr.eleccart.presentation.view.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azizahfzahrr.eleccart.data.local.entity.CartItem
import com.azizahfzahrr.eleccart.data.model.Item
import com.azizahfzahrr.eleccart.data.model.Order
import com.azizahfzahrr.eleccart.data.model.OrderResponse
import com.azizahfzahrr.eleccart.data.repository.CartRepository
import com.azizahfzahrr.eleccart.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

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
    suspend fun createOrder(orderRequest: Order): OrderResponse {
        return orderRepository.createOrder(orderRequest)
    }
    fun prepareOrderRequest(): Order {
        val orderItems = allCartItems.value.mapNotNull { cartItem ->
            val productIdInt = cartItem.productId.toIntOrNull()
            if (productIdInt != null) {
                Item(
                    id = productIdInt,
                    name = cartItem.title ?: "",
                    price = cartItem.price ?: 0,
                    quantity = cartItem.quantity ?: 1
                )
            } else {
                null
            }
        }
        val totalAmount = orderItems.sumOf { it.price * it.quantity }
        return Order(
            amount = totalAmount,
            email = "test@gmail.com",
            items = orderItems
        )
    }
}
