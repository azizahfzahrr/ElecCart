package com.azizahfzahrr.eleccart.presentation.view.detailproduct

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azizahfzahrr.eleccart.data.model.ProductDto
import com.azizahfzahrr.eleccart.data.repository.ProductRepository
import com.azizahfzahrr.eleccart.domain.model.ProductState
import com.azizahfzahrr.eleccart.domain.model.Products
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _productState = MutableLiveData<ProductState>(ProductState.Loading)
    val productState: LiveData<ProductState> get() = _productState

    fun loadProductDetail(id: Int) {
        viewModelScope.launch {
            _productState.value = ProductState.Loading
            try {
                val productDto = repository.getProductDetail(id)

                if (productDto != null) {
                    val product = mapProductDtoToProducts(productDto)
                    _productState.value = ProductState.SuccessDetail(product)
                } else {
                    _productState.value = ProductState.Error("Product not found")
                }
            } catch (e: Exception) {
                _productState.value = ProductState.Error("Failed to fetch product details: ${e.message}")
            }
        }
    }

    private fun mapProductDtoToProducts(productDto: ProductDto): Products {
        val firstData = productDto.data?.firstOrNull()
        return Products(
            id = firstData?.pdId ?: 0,
            name = firstData?.pdName,
            price = firstData?.pdPrice ?: 0,
            description = firstData?.pdDescription,
            category = firstData?.categories?.firstOrNull()?.ctName,
            image = firstData?.pdImageUrl,
            quantity = firstData?.pdQuantity ?: 0,
            averageRating = firstData?.totalAverageRating ?: 0.0,
            totalReviews = firstData?.totalReviews
        )
    }
}