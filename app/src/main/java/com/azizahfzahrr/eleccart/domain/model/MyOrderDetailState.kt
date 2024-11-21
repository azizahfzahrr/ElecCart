package com.azizahfzahrr.eleccart.domain.model

import com.azizahfzahrr.eleccart.data.model.MyOrderDetailResponse

sealed class MyOrderDetailState {
    object Loading : MyOrderDetailState()
    data class Success(val orderDetails: MyOrderDetailResponse.Data?) : MyOrderDetailState()
    data class Error(val message: String) : MyOrderDetailState()
}
