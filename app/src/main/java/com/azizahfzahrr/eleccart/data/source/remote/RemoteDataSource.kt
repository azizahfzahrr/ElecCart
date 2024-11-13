package com.azizahfzahrr.eleccart.data.source.remote

import android.util.Log
import com.azizahfzahrr.eleccart.data.model.CartResponse
import com.azizahfzahrr.eleccart.data.model.ProductRequest
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import javax.inject.Inject

interface RemoteDataSource {
    suspend fun fetchAllProducts(page: Int, limit: Int? = null, sort: String? = null): ProductsResponse
    suspend fun fetchProductDetail(id: Int): ProductsResponse.Product
    suspend fun fetchProductsByCategory(category: String): ProductsResponse
    suspend fun fetchAllCategories(): List<String>
    suspend fun postProduct(productRequest: ProductRequest)
}

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : RemoteDataSource {

    override suspend fun fetchAllProducts(page: Int, limit: Int?, sort: String?): ProductsResponse {
        val response = apiService.getAllProducts(page, limit, sort)
        return response ?: ProductsResponse(message = "No products found", products = emptyList(), status = "error")
    }

    override suspend fun fetchProductDetail(id: Int): ProductsResponse.Product {
        return apiService.getProductsDetail(id)
    }

//    override suspend fun fetchProductsByCategory(category: String): ProductsResponse {
//        val response = apiService.getProductsByCategory(category)
//        Log.d("RemoteDataSourceImpl", "Fetched products for category $category: ${response.products}")
//        return response
//    }
    override suspend fun fetchProductsByCategory(category: String): ProductsResponse {
        val response = apiService.getProductsByCategory(category)
        val products = response.products?.filterNotNull() ?: emptyList()
        Log.d("RemoteDataSourceImpl", "Fetched products for category $category: $products")
        return response.copy(products = products)
    }


    override suspend fun fetchAllCategories(): List<String> {
        val response = apiService.getAllCategories()
        return response.categories?.filterNotNull() ?: emptyList()
    }

    override suspend fun postProduct(productRequest: ProductRequest) {
        apiService.addProduct(productRequest)
    }
}

