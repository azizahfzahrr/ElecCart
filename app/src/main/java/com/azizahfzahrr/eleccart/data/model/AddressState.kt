package com.azizahfzahrr.eleccart.data.model

sealed class AddressState {
    data object Loading : AddressState()
    data class Success(val address: List<Address>) : AddressState()
    data class Error(val message: String) : AddressState()
}
