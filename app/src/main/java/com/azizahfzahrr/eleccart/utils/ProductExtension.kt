package com.azizahfzahrr.eleccart.utils

import com.azizahfzahrr.eleccart.data.model.ProductDto
import com.azizahfzahrr.eleccart.domain.model.Products

fun ProductDto.Data.toProduct(): Products {
    return Products(
        id = this.pdId ?: 0,
        name = this.pdName ?: "",
        price = this.pdPrice ?: 0,
        description = this.pdDescription ?: "",
        category = this.categories?.firstOrNull()?.ctName ?: "No Category",
        image = this.pdImageUrl ?: "",
        quantity = this.pdQuantity ?: 0,
        averageRating = this.totalAverageRating ?: 0.0,
        totalReviews = this.totalReviews ?: ""
    )
}