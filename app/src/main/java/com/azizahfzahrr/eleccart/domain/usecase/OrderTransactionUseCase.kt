package com.azizahfzahrr.eleccart.domain.usecase

import com.azizahfzahrr.eleccart.data.model.MyOrderDetailResponse
import com.azizahfzahrr.eleccart.data.repository.OrderTransactionRepository
import com.azizahfzahrr.eleccart.domain.model.MyOrderResponse
import com.azizahfzahrr.eleccart.domain.model.OrderTransaction
import javax.inject.Inject

class OrderTransactionUseCase @Inject constructor(
    private val orderTransactionRepository: OrderTransactionRepository
) {
    suspend operator fun invoke(email: String): List<MyOrderResponse.Data?>{
        return orderTransactionRepository.getAllOrderTransaction(email)
    }
    suspend fun getOrderDetailsById(orderId: String): MyOrderDetailResponse.Data? {
        return orderTransactionRepository.getOrderTransactionById(orderId)
    }
}