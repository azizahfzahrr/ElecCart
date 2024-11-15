package com.azizahfzahrr.eleccart.presentation.view.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azizahfzahrr.eleccart.data.model.CategoryData
import com.azizahfzahrr.eleccart.data.model.ProductDto
import com.azizahfzahrr.eleccart.data.model.ProductsResponse
import com.azizahfzahrr.eleccart.domain.usecase.ProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productUseCase: ProductUseCase
) : ViewModel() {

    private val _products = MutableLiveData<List<ProductDto.Data?>>()
    val products: LiveData<List<ProductDto.Data?>> = _products

    private val _categoryData = MutableLiveData<List<CategoryData.Product?>>()
    val categoryData: LiveData<List<CategoryData.Product?>> = _categoryData

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    var isLastPage = false
    private var currentPage = 1
    private val pageSize = 10

    fun loadAllProducts() {
        if (_loading.value == true || isLastPage) return

        _loading.value = true
        viewModelScope.launch {
            try {
                val productsResponse = productUseCase.getAllProducts()
                val newProducts = productsResponse.data ?: emptyList()

                if (newProducts.isNotEmpty()) {
                    _products.value = (_products.value ?: emptyList()) + newProducts
                    currentPage++
                } else {
                    isLastPage = true
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadProductsByCategory(category: String) {
        if (_loading.value == true || isLastPage) return

        _loading.value = true
        resetPagination()
        viewModelScope.launch {
            try {
                Log.d("HomeViewModel", "Fetching products for category:")
                val productsResponse = productUseCase.getProductsByCategory(category)
                val newProducts: List<CategoryData.Product?>? = productsResponse.data?.get(0)?.products

                Log.d("HomeViewModel", "Fetched products for category: $newProducts")

                if (newProducts != null) {
                    if (newProducts.isNotEmpty()) {
                        _categoryData.value = newProducts
                        currentPage++
                    } else {
                        isLastPage = true
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearProducts() {
        _products.value = emptyList()
    }

    fun resetPagination() {
        currentPage = 1
        isLastPage = false
    }
}