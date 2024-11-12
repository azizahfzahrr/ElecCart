package com.azizahfzahrr.eleccart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey
    val productId: String,
    val title: String?,
    val price: Int?,
    val image: String?,
    var quantity: Int? = 1,
    val isSelected: Boolean = false
)
