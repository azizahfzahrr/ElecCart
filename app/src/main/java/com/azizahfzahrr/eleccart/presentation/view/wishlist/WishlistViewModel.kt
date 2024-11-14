package com.azizahfzahrr.eleccart.presentation.view.wishlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.azizahfzahrr.eleccart.data.repository.WishlistRepository
import com.azizahfzahrr.eleccart.data.source.local.WishlistEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val wishlistRepository: WishlistRepository
) : ViewModel() {

    private val _wishlistProducts = MutableLiveData<List<WishlistEntity>>()
    val wishlistProducts: LiveData<List<WishlistEntity>> get() = _wishlistProducts

    private var cachedWishlist: List<WishlistEntity> = emptyList()

    fun loadWishlistProducts() {
        viewModelScope.launch {
            val products = wishlistRepository.getAllWishlistProducts()
            cachedWishlist = products
            _wishlistProducts.postValue(products)
        }
    }

    fun isInWishlist(productId: String?): LiveData<Boolean> {
        return liveData {
            emit(cachedWishlist.any { it.productId == productId })
        }
    }

    fun addProductToWishlist(product: WishlistEntity) {
        viewModelScope.launch {
            wishlistRepository.addProductToWishlist(product)
            loadWishlistProducts()
        }
    }

    fun removeProductFromWishlist(productId: String?) {
        viewModelScope.launch {
            val product = cachedWishlist.find { it.productId == productId }
            if (product != null) {
                wishlistRepository.removeProductFromWishlist(product)
                loadWishlistProducts()
            }
        }
    }
}



