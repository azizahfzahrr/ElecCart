package com.azizahfzahrr.eleccart.domain.model

import com.azizahfzahrr.eleccart.data.model.OrderDto

sealed class OrderTransactionState {
    data object Loading: OrderTransactionState()
    data class Success(val transactionOrder: List<OrderTransaction>) : OrderTransactionState()
    data class SuccessDetail(val order: OrderTransactionDetail) : OrderTransactionState()
    data class Error(val message: String) : OrderTransactionState()
}