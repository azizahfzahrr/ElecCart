package com.azizahfzahrr.eleccart.data.model


import com.google.gson.annotations.SerializedName

data class TransactionOrderDetailResponse(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("data")
    val `data`: List<Any?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("totalOrders")
    val totalOrders: Int?,
    @SerializedName("totalPages")
    val totalPages: Int?
)