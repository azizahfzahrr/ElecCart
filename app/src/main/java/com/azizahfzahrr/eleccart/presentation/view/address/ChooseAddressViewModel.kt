package com.azizahfzahrr.eleccart.presentation.view.address

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azizahfzahrr.eleccart.data.repository.AddressRepository
import com.azizahfzahrr.eleccart.data.source.local.AddressDatabase
import com.azizahfzahrr.eleccart.data.source.local.AddressEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseAddressViewModel @Inject constructor(
    private val addressRepository: AddressRepository
) : ViewModel() {

    private val _listAddress = MutableLiveData<List<AddressEntity>>()
    val listAddress: LiveData<List<AddressEntity>> get() = _listAddress

    private val _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> get() = _errorState

    init {
        loadAddress()
    }

    fun loadAddress() {
        viewModelScope.launch {
            try {
                val addresses = addressRepository.getAllAddress()
                _listAddress.postValue(addresses)
            } catch (e: Exception) {
                Log.e("ChooseAddressViewModel", "Error loading addresses", e)
                _errorState.postValue(e.message ?: "An error occurred while loading addresses")
            }
        }
    }

    fun deleteAddress(address: AddressEntity, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                addressRepository.deleteAddress(address)
                loadAddress()
                onSuccess()
            } catch (e: Exception) {
                Log.e("ChooseAddressViewModel", "Error deleting address", e)
                _errorState.postValue(e.message ?: "An error occurred while deleting the address")
            }
        }
    }
}
