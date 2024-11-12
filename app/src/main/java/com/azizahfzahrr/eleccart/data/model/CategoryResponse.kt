package com.azizahfzahrr.eleccart.data.model


import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("categories")
    val categories: List<String?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: String?
)