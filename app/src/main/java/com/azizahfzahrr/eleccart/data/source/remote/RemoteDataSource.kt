package com.azizahfzahrr.eleccart.data.source.remote

import android.util.Log
import com.azizahfzahrr.eleccart.data.model.CategoryDto
import com.azizahfzahrr.eleccart.data.model.MyOrderDetailResponse
import com.azizahfzahrr.eleccart.data.model.ProductDto
import com.azizahfzahrr.eleccart.data.model.ProductRequest
import com.azizahfzahrr.eleccart.domain.model.MyOrderResponse

import javax.inject.Inject

interface RemoteDataSource {
    suspend fun getAllProductsSearch(search: String?, limit: Int?): ProductDto
    suspend fun fetchAllProducts(): ProductDto
    suspend fun fetchProductDetail(id: Int): ProductDto
    suspend fun fetchProductsByCategory(category: String): CategoryDto
    suspend fun fetchAllCategories(): List<String>
    suspend fun postProduct(productRequest: ProductRequest)
    suspend fun getAllOrdersTransaction(): MyOrderResponse
    suspend fun getOrderTransactionById(orderId: String): MyOrderDetailResponse
}

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : RemoteDataSource {

    override suspend fun getAllProductsSearch(search: String?, limit: Int?): ProductDto {
        return apiService.getAllProductsSearch(search, limit)
    }

    override suspend fun fetchAllProducts(): ProductDto {
        val response = apiService.getAllProducts()
        return response
    }

    override suspend fun fetchProductDetail(id: Int): ProductDto {
        return apiService.getProductsDetail(id)
    }

    override suspend fun fetchProductsByCategory(category: String): CategoryDto {
        val response = apiService.getCategory(category)
        val products = response.data?.filterNotNull() ?: emptyList()
        Log.d("RemoteDataSourceImpl", "Fetched products for category $response")
        return response.copy(data = products)
    }

    override suspend fun fetchAllCategories(): List<String> {
        val response = apiService.getAllCategories()
        return response.categories?.filterNotNull() ?: emptyList()
    }

    override suspend fun postProduct(productRequest: ProductRequest) {
        apiService.addProduct(productRequest)
    }

    override suspend fun getAllOrdersTransaction(): MyOrderResponse {
        return apiService.getAllOrdersTransaction()
    }

    override suspend fun getOrderTransactionById(orderId: String): MyOrderDetailResponse {
        return apiService.getOrderTransactionById(orderId)
    }
}

