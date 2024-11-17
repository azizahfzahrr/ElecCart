package com.azizahfzahrr.eleccart.presentation.view.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azizahfzahrr.eleccart.data.local.entity.CartItem
import com.azizahfzahrr.eleccart.data.model.Item
import com.azizahfzahrr.eleccart.data.model.Order
import com.azizahfzahrr.eleccart.data.model.OrderDto
import com.azizahfzahrr.eleccart.data.repository.CartRepository
import com.azizahfzahrr.eleccart.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _allCartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val allCartItems: StateFlow<List<CartItem>> get() = _allCartItems

    private val _totalAmount = MutableStateFlow<Int>(0)
    val totalAmount: StateFlow<Int> get() = _totalAmount

    private val _totalItems = MutableStateFlow<Int>(0)
    val totalItems: StateFlow<Int> get() = _totalItems

    init {
        loadCartItems()
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            cartRepository.getAllCartItems().collect { items ->
                _allCartItems.value = items
                updateOrderSummary(items)
            }
        }
    }

    fun addItemToCart(cartItem: CartItem) {
        viewModelScope.launch {
            cartRepository.addSingleCartItem(cartItem)
            loadCartItems()
        }
    }

    fun updateItemInCart(cartItem: CartItem) {
        viewModelScope.launch {
            cartRepository.updateCartItem(cartItem)
            loadCartItems()
        }
    }

    fun deleteItemFromCart(cartItem: CartItem) {
        viewModelScope.launch {
            cartRepository.deleteCartItem(cartItem)
            loadCartItems()
        }
    }

    fun deleteAllItemsFromCart() {
        viewModelScope.launch {
            cartRepository.deleteAllCartItems()
            loadCartItems()
        }
    }

    fun prepareOrderRequest(): Order {
        val orderItems = _allCartItems.value.mapNotNull { cartItem ->
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

    private fun updateOrderSummary(cartItems: List<CartItem>) {
        val selectedItems = cartItems.filter { it.isSelected }
        val totalAmount = selectedItems.sumOf { it.price?.times(it.quantity ?: 1) ?: 0 }
        val totalItems = selectedItems.sumOf { it.quantity ?: 0 }

        _totalAmount.value = totalAmount
        _totalItems.value = totalItems
    }

    suspend fun createOrder(orderRequest: Order): OrderDto {
        return orderRepository.createOrder(orderRequest)
    }
}

