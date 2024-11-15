package com.azizahfzahrr.eleccart.data.source.local

import android.content.Context
import android.content.SharedPreferences
import com.azizahfzahrr.eleccart.data.model.ProductDto
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import com.google.gson.Gson

class CartManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("cart_preferences", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val cartKey = "cart_key"

    fun addProductToCart(product: ProductDto.Data) {
        val cartProductList = getCartProducts().toMutableList()

        if (!cartProductList.any { it.pdId == product.pdId }) {
            cartProductList.add(product)
            sharedPreferences.edit().putString(cartKey, gson.toJson(cartProductList)).apply()
        }
    }

    fun getCartProducts(): List<ProductDto.Data> {
        val json = sharedPreferences.getString(cartKey, "[]")
        return gson.fromJson(json, Array<ProductDto.Data>::class.java).toList()
    }

    fun removeProductFromCart(product: ProductDto.Data) {
        val cartProductList = getCartProducts().toMutableList()
        cartProductList.removeAll { it.pdId == product.pdId }
        sharedPreferences.edit().putString(cartKey, gson.toJson(cartProductList)).apply()
    }

    fun isProductInCart(product: ProductDto.Data): Boolean {
        return getCartProducts().any { it.pdId == product.pdId }
    }
}