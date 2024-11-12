package com.azizahfzahrr.eleccart.data.repository

import com.azizahfzahrr.eleccart.data.local.entity.CartItem
import com.azizahfzahrr.eleccart.data.source.local.CartDao
import kotlinx.coroutines.flow.Flow

class CartRepository(private val cartDao: CartDao) {

    fun getAllCartItems(): Flow<List<CartItem>> = cartDao.getAllCartItems()

    suspend fun insertCartItem(cartItem: CartItem) = cartDao.insertCartItem(cartItem)
    suspend fun updateCartItem(cartItem: CartItem) = cartDao.updateCartItem(cartItem)
    suspend fun deleteCartItem(cartItem: CartItem) = cartDao.deleteCartItem(cartItem)
    suspend fun deleteAllCartItems() = cartDao.deleteAllCartItems()

    suspend fun getCartItemById(productId: String): CartItem? = cartDao.getCartItemById(productId)

    suspend fun addSingleCartItem(cartItem: CartItem) {
        val existingItem = cartDao.getCartItemById(cartItem.productId)
        if (existingItem != null) {
            val updatedItem = existingItem.copy(quantity = (existingItem.quantity ?: 0) + 1)
            cartDao.updateCartItem(updatedItem)
        } else {
            cartDao.insertCartItem(cartItem.copy(quantity = 1))
        }
    }
}

