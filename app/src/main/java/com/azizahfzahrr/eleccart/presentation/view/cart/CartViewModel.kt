package com.azizahfzahrr.eleccart.presentation.view.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azizahfzahrr.eleccart.data.local.entity.CartItem
import com.azizahfzahrr.eleccart.data.model.Item
import com.azizahfzahrr.eleccart.data.model.Order
import com.azizahfzahrr.eleccart.data.model.OrderResponse
import com.azizahfzahrr.eleccart.data.repository.CartRepository
import com.azizahfzahrr.eleccart.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    // Menggunakan StateFlow untuk Cart Items, lebih cocok untuk pengumpulan data
    private val _allCartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val allCartItems: StateFlow<List<CartItem>> get() = _allCartItems

    // Total amount dan item count untuk order summary
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

    // Menghapus item dari cart
    fun deleteItemFromCart(cartItem: CartItem) {
        viewModelScope.launch {
            cartRepository.deleteCartItem(cartItem)
            loadCartItems() // Reload setelah menghapus item
        }
    }

    // Menghapus semua item dari cart
    fun deleteAllItemsFromCart() {
        viewModelScope.launch {
            cartRepository.deleteAllCartItems()
            loadCartItems() // Reload setelah mengosongkan cart
        }
    }

    // Menyiapkan request order
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

    // Fungsi untuk update total harga dan jumlah item
    private fun updateOrderSummary(cartItems: List<CartItem>) {
        val totalAmount = cartItems.sumOf { it.price?.times(it.quantity ?: 1) ?: 0 }
        val totalItems = cartItems.sumOf { it.quantity ?: 0 }

        _totalAmount.value = totalAmount
        _totalItems.value = totalItems
    }

    // Fungsi untuk melakukan pemesanan
    suspend fun createOrder(orderRequest: Order): OrderResponse {
        return orderRepository.createOrder(orderRequest)
    }
}

