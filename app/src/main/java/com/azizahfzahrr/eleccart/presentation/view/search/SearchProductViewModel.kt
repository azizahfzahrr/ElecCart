package com.azizahfzahrr.eleccart.presentation.view.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azizahfzahrr.eleccart.domain.model.ProductState
import com.azizahfzahrr.eleccart.domain.model.Products
import com.azizahfzahrr.eleccart.domain.usecase.ProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchProductViewModel @Inject constructor(
    private val productUseCase: ProductUseCase
): ViewModel() {

    private val _productState = MutableStateFlow<ProductState>(ProductState.Loading)
    val productState: StateFlow<ProductState> = _productState

    private var currentProductList: List<Products> = emptyList()

    init {
        loadAllProducts(search = null, limit = null)
    }

    fun loadAllProducts(search: String?, limit: Int?) {
        _productState.value = ProductState.Loading
        viewModelScope.launch {
            try {
                val products = productUseCase(search, limit)
                if (products.isNotEmpty()) {
                    currentProductList = products
                    _productState.value = ProductState.Success(products)
                } else {
                    _productState.value = ProductState.Error("Product not found")
                }
            } catch (e: Exception) {
                _productState.value = ProductState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getProductById(id: Int): Products? {
        return currentProductList.find { it.id == id }
    }
}