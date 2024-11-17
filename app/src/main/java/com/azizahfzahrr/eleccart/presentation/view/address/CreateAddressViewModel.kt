package com.azizahfzahrr.eleccart.presentation.view.address

import android.util.Log
import androidx.activity.result.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azizahfzahrr.eleccart.data.model.Address
import com.azizahfzahrr.eleccart.data.repository.AddressRepository
import com.azizahfzahrr.eleccart.data.source.local.AddressDatabase
import com.azizahfzahrr.eleccart.data.source.local.AddressEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAddressViewModel @Inject constructor(
    private val addressRepository: AddressRepository
): ViewModel(){

    private val _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> get() = _errorState

    fun addAddress(address: AddressEntity, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                addressRepository.insertAddress(address)
                onSuccess()
            } catch (e: Exception) {
                Log.e("AddAddressViewModel", "Error adding address", e)
                _errorState.value = e.message ?: "An error occurred"
            }
        }
    }

    fun updateAddress(address: AddressEntity, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                addressRepository.updateAddress(address)
                onSuccess()
            } catch (e: Exception) {
                Log.e("UpdateAddressViewModel", "Error updating address", e)
                _errorState.value = e.message ?: "An error occurred"
            }
        }
    }

    fun addOrUpdateAddress(address: AddressEntity, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                addressRepository.addSingleAddress(address)
                onSuccess()
            } catch (e: Exception) {
                Log.e("AddOrUpdateAddressViewModel", "Error in addOrUpdateAddress", e)
                _errorState.value = e.message ?: "An error occurred while processing the address"
            }
        }
    }
}