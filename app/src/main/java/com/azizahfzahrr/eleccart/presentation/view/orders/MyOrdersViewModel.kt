package com.azizahfzahrr.eleccart.presentation.view.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azizahfzahrr.eleccart.domain.model.MyOrderState
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

    private val _orderTransactionState = MutableStateFlow<MyOrderState>(MyOrderState.Loading)
    val orderTransactionState: StateFlow<MyOrderState> = _orderTransactionState

    fun loadAllOrderTransaction() {
        _orderTransactionState.value = MyOrderState.Loading
        viewModelScope.launch {
            try {
                val transactionOrder = orderTransactionUseCase()
                _orderTransactionState.value = if (transactionOrder.isNotEmpty()) {
                    MyOrderState.Success(transactionOrder)
                } else {
                    MyOrderState.Error("No Transaction")
                }
            } catch (e: Exception) {
                _orderTransactionState.value = MyOrderState.Error(e.message ?: "Error")
            }
        }
    }

}