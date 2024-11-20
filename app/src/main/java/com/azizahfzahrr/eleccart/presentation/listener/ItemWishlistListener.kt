package com.azizahfzahrr.eleccart.presentation.listener

import com.azizahfzahrr.eleccart.data.source.local.WishlistEntity

interface ItemWishlistListener {
    fun onAddToCartClicked(product: WishlistEntity)
    fun onDeleteFromWishlistClicked(productId: String)
}