package com.azizahfzahrr.eleccart.domain.usecase

import com.azizahfzahrr.eleccart.data.model.CategoryDto
import com.azizahfzahrr.eleccart.data.model.ProductDto
import com.azizahfzahrr.eleccart.data.model.ProductRequest
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import com.azizahfzahrr.eleccart.data.repository.ProductRepository
import com.azizahfzahrr.eleccart.domain.model.Products
import javax.inject.Inject

class ProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {

    suspend operator fun invoke(search: String?, limit: Int?): List<Products>{
        return productRepository.getAllProductsSearch(search, limit)
    }

    suspend fun getAllProducts(): ProductDto {
        return productRepository.getAllProducts()
    }

    suspend fun getProductDetail(id: Int): ProductDto {
        return productRepository.getProductDetail(id)
    }

    suspend fun getProductsByCategory(category: String): CategoryDto {
        return productRepository.getCategory(category)
    }

    suspend fun getAllCategories(): List<String> {
        return productRepository.getAllCategories()
    }

    suspend fun addProduct(productRequest: ProductRequest) {
        productRepository.addProduct(productRequest)
    }
}
