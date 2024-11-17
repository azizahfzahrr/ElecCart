package com.azizahfzahrr.eleccart.presentation.listener

import com.azizahfzahrr.eleccart.data.source.local.AddressEntity

interface ItemAddressListener {
    fun onDelete(address: AddressEntity)
    fun onEdit(address: AddressEntity)
}