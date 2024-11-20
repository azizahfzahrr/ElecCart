package com.azizahfzahrr.eleccart.domain.usecase

import com.azizahfzahrr.eleccart.data.repository.OrderTransactionRepository
import com.azizahfzahrr.eleccart.domain.model.OrderTransactionDetail
import javax.inject.Inject

class OrderTransactionDetailById @Inject constructor(
    private val orderTransactionRepository: OrderTransactionRepository
){
//    suspend operator fun invoke(id: String): OrderTransactionDetail {
//        return orderTransactionRepository.getOrderTransactionById(id)
//    }
}