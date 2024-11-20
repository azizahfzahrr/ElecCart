package com.azizahfzahrr.eleccart.data.repository

import com.azizahfzahrr.eleccart.data.source.remote.RemoteDataSource
import com.azizahfzahrr.eleccart.domain.model.MyOrderResponse
import com.azizahfzahrr.eleccart.domain.model.OrderTransaction
import com.azizahfzahrr.eleccart.domain.model.OrderTransactionDetail
import javax.inject.Inject

interface OrderTransactionRepository {
    suspend fun getAllOrderTransaction(): List<MyOrderResponse.Data?>
  //  suspend fun getOrderTransactionById(id: String): OrderTransactionDetail
}

class OrderTransactionRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): OrderTransactionRepository {

    override suspend fun getAllOrderTransaction(): List<MyOrderResponse.Data?> {
        val response = remoteDataSource.getAllOrdersTransaction()

        return response.data ?: emptyList()
    }

//    override suspend fun getOrderTransactionById(id: String): OrderTransactionDetail {
//        val response = remoteDataSource.
//    }
}