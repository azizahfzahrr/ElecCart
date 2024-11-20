package com.azizahfzahrr.eleccart.data.model

import java.io.Serializable

data class Item(
    val id: Int,
    val name: String,
    val price: Int,
    val url: String,
    val quantity: Int
) : Serializable
