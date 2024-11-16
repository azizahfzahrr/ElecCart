package com.azizahfzahrr.eleccart.data.repository

import com.azizahfzahrr.eleccart.data.model.Order
import com.azizahfzahrr.eleccart.data.model.OrderDto
import com.azizahfzahrr.eleccart.data.source.remote.ApiService
import javax.inject.Inject

class OrderRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun createOrder(orderRequest: Order): OrderDto{
        return apiService.createOrder(orderRequest)
    }
}