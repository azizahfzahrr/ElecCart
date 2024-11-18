package com.azizahfzahrr.eleccart.domain.model

sealed class OrderTransactionState {
    data object Loading: OrderTransactionState()
    data class Success(val transactionOrder: List<OrderTransaction>) : OrderTransactionState()
    data class SuccessDetail(val order: OrderTransactionDetail) : OrderTransactionState()
    data class Error(val message: String) : OrderTransactionState()
}