package com.azizahfzahrr.eleccart.data.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ProductDto(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("data")
    val `data`: List<Data?>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("totalPages")
    val totalPages: Int?,
    @SerializedName("totalProducts")
    val totalProducts: Int?
) {
    data class Data(
        @SerializedName("categories")
        val categories: List<Category?>? = null,
        @SerializedName("pd_data")
        val pdData: PdData?,
        @SerializedName("pd_description")
        val pdDescription: String?,
        @SerializedName("pd_id")
        val pdId: Int?,
        @SerializedName("pd_image_url")
        val pdImageUrl: String?,
        @SerializedName("pd_name")
        val pdName: String?,
        @SerializedName("pd_price")
        val pdPrice: Int?,
        @SerializedName("pd_quantity")
        val pdQuantity: Int?,
        @SerializedName("total_average_rating")
        val totalAverageRating: Any?,
        @SerializedName("total_reviews")
        val totalReviews: String?
    ): Serializable {
        data class Category(
            @SerializedName("ct_id")
            val ctId: Int?,
            @SerializedName("ct_name")
            val ctName: String?
        )

        data class PdData(
            @SerializedName("brand")
            val brand: String?,
            @SerializedName("color")
            val color: String?,
            @SerializedName("discount")
            val discount: String?,
            @SerializedName("model")
            val model: String?,
            @SerializedName("on_sale")
            val onSale: String?,
            @SerializedName("popular")
            val popular: String?
        )
    }
}