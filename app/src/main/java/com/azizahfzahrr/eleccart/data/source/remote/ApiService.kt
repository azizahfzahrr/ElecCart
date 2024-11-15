package com.azizahfzahrr.eleccart.data.source.remote

import com.azizahfzahrr.eleccart.data.model.CartResponse
import com.azizahfzahrr.eleccart.data.model.CategoryDto
import com.azizahfzahrr.eleccart.data.model.CategoryResponse
import com.azizahfzahrr.eleccart.data.model.Order
import com.azizahfzahrr.eleccart.data.model.OrderResponse
import com.azizahfzahrr.eleccart.data.model.ProductDto
import com.azizahfzahrr.eleccart.data.model.ProductRequest
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import com.azizahfzahrr.eleccart.data.model.UserRequest
import com.azizahfzahrr.eleccart.data.model.UserResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

//    @GET("products")
//    suspend fun getAllProduct(
//        @Query("page") page: Int? = null,
//        @Query("limit") limit: Int? = null,
//        @Query("sort") sort: String? = null
//    ): ProductsResponse

    @GET("/phincon/api/products")
    suspend fun getAllProducts(): ProductDto

    @GET("/phincon/api/products/category/{category}")
    suspend fun getCategory(
        @Path ("category") category: String
    ): CategoryDto

    @GET("products/{id}")
    suspend fun getProductsDetail(@Path("id") id: Int): ProductsResponse.Product

//    @GET("products/category")
//    suspend fun getProductsByCategory(
//        @Query("type") category: String,
//    ): ProductsResponse

    @GET("products/category")
    suspend fun getAllCategories(): CategoryResponse

    @POST("products")
    suspend fun addProduct(@Body productRequest: ProductRequest)

    @POST("order/snap")
    suspend fun createOrder(
        @Body orderRequest: Order
    ): OrderResponse

    @GET("users")
    suspend fun getAllUsers(): List<UserResponse>

    @GET("users/{id}")
    suspend fun getUserDetail(
        @Path("id") id: Int
    ): UserResponse

    @POST("users")
    suspend fun addUser(
        @Body userRequest: UserRequest
    ): UserResponse

    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body userRequest: UserRequest
    ): UserResponse

    @DELETE("users/{id}")
    suspend fun deleteUser(
        @Path("id") id: Int
    ): UserResponse
}