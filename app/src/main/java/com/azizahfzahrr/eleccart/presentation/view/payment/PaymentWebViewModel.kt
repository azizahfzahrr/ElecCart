package com.azizahfzahrr.eleccart.presentation.view.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azizahfzahrr.eleccart.data.local.entity.CartItem
import com.azizahfzahrr.eleccart.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentWebViewModel @Inject constructor(
    private val cartRepository: CartRepository
): ViewModel() {

    fun clearCart(){
        viewModelScope.launch {
            cartRepository.deleteAllCartItems()
        }
    }
}