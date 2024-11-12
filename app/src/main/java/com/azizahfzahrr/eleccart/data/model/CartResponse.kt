package com.azizahfzahrr.eleccart.data.model

data class CartResponse(
    val message: String?,
    val cartItems: List<ProductCart>,
    val status: String?
)