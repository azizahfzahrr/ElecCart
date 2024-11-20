package com.azizahfzahrr.eleccart.presentation.view.wishlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.azizahfzahrr.eleccart.data.local.entity.CartItem
import com.azizahfzahrr.eleccart.data.repository.CartRepository
import com.azizahfzahrr.eleccart.data.repository.WishlistRepository
import com.azizahfzahrr.eleccart.data.source.local.WishlistEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val wishlistRepository: WishlistRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _wishlistProducts = MutableLiveData<List<WishlistEntity>>()
    val wishlistProducts: LiveData<List<WishlistEntity>> get() = _wishlistProducts

    private var cachedWishlist: List<WishlistEntity> = emptyList()

    fun loadWishlistProducts() {
        viewModelScope.launch {
            val products = wishlistRepository.getAllWishlistProducts().map { product ->
                product.copy(isInWishlist = true)
            }
            cachedWishlist = products
            _wishlistProducts.postValue(products)

        }
    }

    fun isInWishlist(productId: String): LiveData<Boolean> {
        return liveData {
            val isInWishlist = wishlistRepository.isProductInWishlist(productId)
            emit(isInWishlist)
        }
    }

    fun addProductToWishlist(product: WishlistEntity) {
        viewModelScope.launch {
            wishlistRepository.addProductToWishlist(product.copy(isInWishlist = true))
            _wishlistProducts.value = cachedWishlist + product.copy(isInWishlist = true)
        }
    }

    fun addProductToCart(product: WishlistEntity) {
        viewModelScope.launch {
            val cartItem = CartItem(
                productId = product.productId,
                title = product.title,
                price = product.price,
                image = product.image,
                quantity = 1
            )
            cartRepository.addSingleCartItem(cartItem)
        }
    }

    fun removeProductFromWishlist(productId: String?) {
        viewModelScope.launch {
            val productToRemove = cachedWishlist.find { it.productId == productId }
            if (productToRemove != null) {
                wishlistRepository.removeProductFromWishlist(productToRemove)
                val updateList = cachedWishlist.filter { it.productId != productId }
                _wishlistProducts.value = updateList
                cachedWishlist = updateList
            }
        }
    }
}