package com.azizahfzahrr.eleccart.presentation.view.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azizahfzahrr.eleccart.domain.model.OrderTransactionState
import com.azizahfzahrr.eleccart.domain.usecase.OrderTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    private val orderTransactionUseCase: OrderTransactionUseCase
): ViewModel(){

    private val _orderTransactionState = MutableStateFlow<OrderTransactionState>(OrderTransactionState.Loading)
    val orderTransactionState: StateFlow<OrderTransactionState> = _orderTransactionState

    fun loadAllOrderTransaction() {
        _orderTransactionState.value = OrderTransactionState.Loading
        viewModelScope.launch {
            try {
                val transactionOrder = orderTransactionUseCase()
                _orderTransactionState.value = if (transactionOrder.isNotEmpty()) {
                    OrderTransactionState.Success(transactionOrder)
                } else {
                    OrderTransactionState.Error("No Transaction")
                }
            } catch (e: Exception) {
                _orderTransactionState.value = OrderTransactionState.Error(e.message ?: "Error")
            }
        }
    }

}