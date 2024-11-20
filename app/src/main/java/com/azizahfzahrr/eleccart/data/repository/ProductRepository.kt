package com.azizahfzahrr.eleccart.data.repository

import android.util.Log
import android.widget.Toast
import com.azizahfzahrr.eleccart.data.model.CartResponse
import com.azizahfzahrr.eleccart.data.model.CategoryDto
import com.azizahfzahrr.eleccart.data.model.ProductDto
import com.azizahfzahrr.eleccart.data.model.ProductRequest
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import com.azizahfzahrr.eleccart.data.source.remote.RemoteDataSource
import com.azizahfzahrr.eleccart.domain.model.Products
import javax.inject.Inject

interface ProductRepository {
    suspend fun getAllProductsSearch(search: String?, limit: Int?): List<Products>
    suspend fun getAllProducts(): ProductDto
    suspend fun getProductDetail(id: Int): ProductDto
    suspend fun getCategory(category: String): CategoryDto
    suspend fun getAllCategories(): List<String>
    suspend fun addProduct(productRequest: ProductRequest)
}

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : ProductRepository {

    override suspend fun getAllProductsSearch(search: String?, limit: Int?): List<Products> {
        return remoteDataSource.getAllProductsSearch(search, limit).data?.mapNotNull { it?.toProduct() } ?: emptyList()
    }

    override suspend fun getAllProducts(): ProductDto {
        return try {
            remoteDataSource.fetchAllProducts()
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "Error fetching all products: ${e.message}")
            ProductDto(message = "Error fetching products", data = emptyList(), status = "error", totalPages = 0, totalProducts = 0, code = 0)
        }
    }


    override suspend fun getProductDetail(id: Int): ProductDto {
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
}
