package com.azizahfzahrr.eleccart.domain.model

sealed class MyOrderState {
    data object Loading: MyOrderState()
    data class Success(val transactionOrder: List<MyOrderResponse.Data?>) : MyOrderState()
    data class SuccessDetail(val order: OrderTransactionDetail) : MyOrderState()
    data class Error(val message: String) : MyOrderState()
}