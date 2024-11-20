package com.azizahfzahrr.eleccart.domain.model

sealed class ProductState {
    data object Loading: ProductState()
    data class Success(val products: List<Products>): ProductState()
    data class SuccessDetail(val products: Products): ProductState()
    data class Error(val message: String): ProductState()
}