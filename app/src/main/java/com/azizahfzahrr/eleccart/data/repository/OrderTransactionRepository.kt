package com.azizahfzahrr.eleccart.data.repository

import com.azizahfzahrr.eleccart.data.source.remote.RemoteDataSource
import com.azizahfzahrr.eleccart.domain.model.OrderTransaction
import com.azizahfzahrr.eleccart.domain.model.OrderTransactionDetail
import javax.inject.Inject

interface OrderTransactionRepository {
    suspend fun getAllOrderTransaction(): List<OrderTransaction>
    suspend fun getOrderTransactionById(id: String): OrderTransactionDetail
}

class OrderTransactionRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): OrderTransactionRepository {

    override suspend fun getAllOrderTransaction(): List<OrderTransaction> {
        val response = remoteDataSource.getAllOrdersTransaction()

        return response.data?.let { data ->
            listOf(
                OrderTransaction(
                    id = (data.orId ?: 0).toString(),
                    totalPrice = data.orTotalPrice ?: 0,
                    status = data.orStatus.orEmpty(),
                )
            )
        } ?: emptyList()
    }

    override suspend fun getOrderTransactionById(id: String): OrderTransactionDetail {
        TODO("Not yet implemented")
    }
}