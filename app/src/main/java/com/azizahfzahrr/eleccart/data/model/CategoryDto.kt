package com.azizahfzahrr.eleccart.data.model


import com.google.gson.annotations.SerializedName
import java.util.Locale.Category

data class CategoryDto(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("data")
    val `data`: List<CategoryData?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("totalProducts")
    val totalProducts: Int?
)