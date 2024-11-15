package com.azizahfzahrr.eleccart.data.repository

import android.util.Log
import android.widget.Toast
import com.azizahfzahrr.eleccart.data.model.CartResponse
import com.azizahfzahrr.eleccart.data.model.CategoryDto
import com.azizahfzahrr.eleccart.data.model.ProductDto
import com.azizahfzahrr.eleccart.data.model.ProductRequest
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import com.azizahfzahrr.eleccart.data.source.remote.RemoteDataSource
import javax.inject.Inject

interface ProductRepository {
    suspend fun getAllProducts(): ProductDto
    suspend fun getProductDetail(id: Int): ProductsResponse.Product
    suspend fun getCategory(category: String): CategoryDto
    suspend fun getAllCategories(): List<String>
    suspend fun addProduct(productRequest: ProductRequest)
}

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : ProductRepository {

    override suspend fun getAllProducts(): ProductDto {
        return try {
            remoteDataSource.fetchAllProducts()
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "Error fetching all products: ${e.message}")
            ProductDto(message = "Error fetching products", data = emptyList(), status = "error", totalPages = 0, totalProducts = 0, code = 0)
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

    override suspend fun getCategory(category: String): CategoryDto {
        return try {
            remoteDataSource.fetchProductsByCategory(category)
    } catch (e: Exception){
        Log.e("ProductRepositoryImpl", "Error fetching products by category: ${e.message}")
            CategoryDto(message = "Error fetching products", data = emptyList(), status = "error", totalProducts = 0, code = 0)
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
