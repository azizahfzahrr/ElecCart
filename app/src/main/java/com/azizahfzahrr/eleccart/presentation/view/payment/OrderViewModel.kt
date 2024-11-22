package com.azizahfzahrr.eleccart.presentation.view.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azizahfzahrr.eleccart.data.model.Order
import com.azizahfzahrr.eleccart.data.repository.OrderRepository
import com.azizahfzahrr.eleccart.domain.model.OrderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val orderRepository: OrderRepository
): ViewModel(){
    private val _order = MutableLiveData<Order>()
    val order get() = _order

    private val _orderState = MutableStateFlow<OrderState>(OrderState.Loading)
    val orderState: StateFlow<OrderState> get() = _orderState

    fun createOrder(orderRequest: Order) {
        try {
            viewModelScope.launch {
                val response = orderRepository.createOrder(orderRequest)
                if (response.status == "success") {
                    val paymentUrl = response.data?.transaction?.redirectUrl
                    val token = response.data?.transaction?.token

                    if (paymentUrl != null && token != null) {
                        val paymentUrlWithToken = "$paymentUrl?token=$token"
                        _orderState.value = OrderState.SuccessPayment(paymentUrlWithToken)
                    } else {
                        _orderState.value = OrderState.Success(response)
                    }
                } else {
                    _orderState.value = OrderState.Error("Order creation failed")
                }

            }
        }catch (e: Exception){
            _orderState.value = OrderState.Error(e.message ?: "Unknown error")
        }
    }
}