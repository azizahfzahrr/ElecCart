package com.azizahfzahrr.eleccart.data.model

data class ProductRequest(
    val title: String,
    val brand: String,
    val model: String,
    val color: String,
    val category: String,
    val discount: Int
)