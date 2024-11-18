package com.azizahfzahrr.eleccart.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderTransactionDetail(
    val id: String,
    val status: String,
    val totalPrice: Int,
    val orderDetail: List<Product>
): Parcelable{
    @Parcelize
    data class Product(
        val id: String,
        val name: String,
        val price: Int,
    ): Parcelable
}