package com.azizahfzahrr.eleccart.presentation.view.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azizahfzahrr.eleccart.domain.model.MyOrderDetailState
import com.azizahfzahrr.eleccart.domain.usecase.OrderTransactionDetailById
import com.azizahfzahrr.eleccart.domain.usecase.OrderTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MyOrdersDetailViewModel @Inject constructor(
    private val orderTransactionUseCase: OrderTransactionUseCase
): ViewModel() {

    private val _orderDetailState = MutableStateFlow<MyOrderDetailState>(MyOrderDetailState.Loading)
    val orderDetailState: StateFlow<MyOrderDetailState> = _orderDetailState

    fun loadOrderDetails(orderId: String) {
        _orderDetailState.value = MyOrderDetailState.Loading
        viewModelScope.launch {
            try {
                val orderDetails = orderTransactionUseCase.getOrderDetailsById(orderId)
                if (orderDetails != null) {
                    _orderDetailState.value = MyOrderDetailState.Success(orderDetails)
                } else {
                    _orderDetailState.value = MyOrderDetailState.Error("Order not found: $orderId")
                }
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    _orderDetailState.value = MyOrderDetailState.Error("Order with ID $orderId not found (404)")
                } else {
                    _orderDetailState.value = MyOrderDetailState.Error(e.message ?: "Unknown error")
                }
            } catch (e: Exception) {
                _orderDetailState.value = MyOrderDetailState.Error(e.message ?: "Error")
            }

        }
    }
}
