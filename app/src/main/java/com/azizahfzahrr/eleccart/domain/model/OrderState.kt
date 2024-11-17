package com.azizahfzahrr.eleccart.domain.model

import com.azizahfzahrr.eleccart.data.model.OrderDto

sealed class OrderState {
    object Loading : OrderState()
    data class Success(val orderResponse: OrderDto) : OrderState()
    data class SuccessPayment(val paymentUrl: String) : OrderState()
    data class Error(val message: String) : OrderState()
}