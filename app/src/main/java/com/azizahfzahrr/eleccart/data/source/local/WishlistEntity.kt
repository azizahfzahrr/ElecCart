package com.azizahfzahrr.eleccart.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist_database")
data class WishlistEntity(
    @PrimaryKey
    val productId: String,
    val title: String?,
    val price: Int?,
    val image: String?,
    var isInWishlist: Boolean
)
