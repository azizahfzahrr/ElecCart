package com.azizahfzahrr.eleccart.data.source.local

import android.content.Context
import android.content.SharedPreferences
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import com.google.gson.Gson

class CartManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("cart_preferences", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val cartKey = "cart_key"

    // Menambahkan produk ke dalam cart
    fun addProductToCart(product: ProductsResponse.Product) {
        val cartProductList = getCartProducts().toMutableList()

        // Cek apakah produk sudah ada di cart
        if (!cartProductList.any { it.id == product.id }) {
            cartProductList.add(product)
            sharedPreferences.edit().putString(cartKey, gson.toJson(cartProductList)).apply()
        }
    }

    // Mengambil semua produk yang ada di dalam cart
    fun getCartProducts(): List<ProductsResponse.Product> {
        val json = sharedPreferences.getString(cartKey, "[]")
        return gson.fromJson(json, Array<ProductsResponse.Product>::class.java).toList()
    }

    // Menghapus produk dari cart
    fun removeProductFromCart(product: ProductsResponse.Product) {
        val cartProductList = getCartProducts().toMutableList()
        cartProductList.removeAll { it.id == product.id }
        sharedPreferences.edit().putString(cartKey, gson.toJson(cartProductList)).apply()
    }

    // Mengecek apakah produk sudah ada di cart
    fun isProductInCart(product: ProductsResponse.Product): Boolean {
        return getCartProducts().any { it.id == product.id }
    }
}