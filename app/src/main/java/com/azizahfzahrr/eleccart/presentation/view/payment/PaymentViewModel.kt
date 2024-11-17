package com.azizahfzahrr.eleccart.presentation.view.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azizahfzahrr.eleccart.data.repository.AddressRepository
import com.azizahfzahrr.eleccart.data.repository.OrderRepository
import com.azizahfzahrr.eleccart.data.source.local.AddressDatabase
import com.azizahfzahrr.eleccart.data.source.local.AddressEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val addressRepository: AddressRepository,
) : ViewModel() {

    private val _addresses = MutableLiveData<List<AddressEntity>>()
    val addresses get() = _addresses

    fun fetchAddresses() {
        viewModelScope.launch {
            val addresses = addressRepository.getAllAddress()
            _addresses.postValue(addresses)
        }
    }
}
