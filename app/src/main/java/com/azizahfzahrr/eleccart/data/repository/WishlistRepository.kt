package com.azizahfzahrr.eleccart.data.repository

import com.azizahfzahrr.eleccart.data.source.local.WishlistDao
import com.azizahfzahrr.eleccart.data.source.local.WishlistEntity
import javax.inject.Inject

class WishlistRepository @Inject constructor(private val wishlistDao: WishlistDao) {

    suspend fun addProductToWishlist(product: WishlistEntity) {
        wishlistDao.addProductToWishlist(product)
    }

     fun getAllWishlistProducts(): List<WishlistEntity> {
        return wishlistDao.getAllWishlistProducts()
    }

    suspend fun removeProductFromWishlist(product: WishlistEntity) {
        wishlistDao.removeProductFromWishlist(product)
    }

    suspend fun isProductInWishlist(productId: String): Boolean {
        return wishlistDao.isProductInWishlist(productId)
    }
}


