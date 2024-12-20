package com.azizahfzahrr.eleccart.data.repository

import com.azizahfzahrr.eleccart.data.model.MyOrderDetailResponse
import com.azizahfzahrr.eleccart.data.source.remote.RemoteDataSource
import com.azizahfzahrr.eleccart.domain.model.MyOrderResponse
import com.azizahfzahrr.eleccart.domain.model.OrderTransaction
import com.azizahfzahrr.eleccart.domain.model.OrderTransactionDetail
import javax.inject.Inject

interface OrderTransactionRepository {
    suspend fun getAllOrderTransaction(email: String): List<MyOrderResponse.Data?>
    suspend fun getOrderTransactionById(orderId: String): MyOrderDetailResponse.Data?
}

class OrderTransactionRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): OrderTransactionRepository {

    override suspend fun getAllOrderTransaction(email: String): List<MyOrderResponse.Data?> {
        val response = remoteDataSource.getAllOrdersTransaction(email)

        return response.data ?: emptyList()
    }

    override suspend fun getOrderTransactionById(orderId: String): MyOrderDetailResponse.Data? {
        val response = remoteDataSource.getOrderTransactionById(orderId)
        return response.data
    }
}