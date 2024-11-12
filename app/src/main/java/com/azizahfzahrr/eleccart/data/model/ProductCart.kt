package com.azizahfzahrr.eleccart.data.model

data class ProductCart(
    val id: String,
    val name: String,
    val model: String,
    val price: Double,
    val quantity: Int,
    var isChecked: Boolean
)