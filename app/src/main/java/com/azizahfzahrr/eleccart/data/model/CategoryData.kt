package com.azizahfzahrr.eleccart.data.model


import com.google.gson.annotations.SerializedName

data class CategoryData(
    @SerializedName("ct_id")
    val ctId: Int?,
    @SerializedName("ct_name")
    val ctName: String?,
    @SerializedName("products")
    val products: List<Product?>?
) {
    data class Product(
        @SerializedName("pd_data")
        val pdData: ProductDto.Data.PdData?,
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
        val totalAverageRating: Double?,
        @SerializedName("total_reviews")
        val totalReviews: String?
    )
}