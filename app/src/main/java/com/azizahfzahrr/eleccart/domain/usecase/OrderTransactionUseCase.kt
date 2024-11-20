package com.azizahfzahrr.eleccart.domain.usecase

import com.azizahfzahrr.eleccart.data.repository.OrderTransactionRepository
import com.azizahfzahrr.eleccart.domain.model.MyOrderResponse
import com.azizahfzahrr.eleccart.domain.model.OrderTransaction
import javax.inject.Inject

class OrderTransactionUseCase @Inject constructor(
    private val orderTransactionRepository: OrderTransactionRepository
) {
    suspend operator fun invoke(): List<MyOrderResponse.Data?>{
        return orderTransactionRepository.getAllOrderTransaction()
    }
}