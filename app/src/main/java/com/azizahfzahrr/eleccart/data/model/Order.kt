package com.azizahfzahrr.eleccart.data.model

import java.io.Serializable

data class Order(
    val amount: Int,
    val email: String,
    val items: List<Item>
) : Serializable