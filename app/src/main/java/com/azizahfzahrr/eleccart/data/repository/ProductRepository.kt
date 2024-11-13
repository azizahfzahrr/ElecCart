package com.azizahfzahrr.eleccart.data.repository

import android.util.Log
import com.azizahfzahrr.eleccart.data.model.CartResponse
import com.azizahfzahrr.eleccart.data.model.ProductRequest
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import com.azizahfzahrr.eleccart.data.source.remote.RemoteDataSource
import javax.inject.Inject

interface ProductRepository {
    suspend fun getAllProducts(page: Int, limit: Int? = null, sort: String? = null): ProductsResponse
    suspend fun getProductDetail(id: Int): ProductsResponse.Product
    suspend fun getProductsByCategory(category: String): ProductsResponse
    suspend fun getAllCategories(): List<String>
    suspend fun addProduct(productRequest: ProductRequest)
}

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : ProductRepository {

    override suspend fun getAllProducts(page: Int, limit: Int?, sort: String?): ProductsResponse {
        return try {
            remoteDataSource.fetchAllProducts(page, limit, sort)
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "Error fetching all products: ${e.message}")
            ProductsResponse(message = "Error fetching products", products = emptyList(), status = "error")
        }
    }

    override suspend fun getProductDetail(id: Int): ProductsResponse.Product {
        return try {
            remoteDataSource.fetchProductDetail(id)
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "Error fetching product detail for ID $id: ${e.message}")
            throw e
        }
    }

    override suspend fun getProductsByCategory(category: String): ProductsResponse {
        return try {
            val formattedCategory = category.trim().lowercase()
            val response = remoteDataSource.fetchProductsByCategory(formattedCategory)
            Log.d("ProductRepositoryImpl", "Fetched products for category $formattedCategory: ${response.products}")
            response
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "Error fetching products for category $category: ${e.message}")
            return ProductsResponse(message = "Error fetching category products", products = emptyList(), status = "error")
        }
    }

    override suspend fun getAllCategories(): List<String> {
        return try {
            remoteDataSource.fetchAllCategories()
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "Error fetching categories: ${e.message}")
            emptyList()
        }
    }

    override suspend fun addProduct(productRequest: ProductRequest) {
        try {
            remoteDataSource.postProduct(productRequest)
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "Error posting new product: ${e.message}")
            throw e
        }
    }
}
