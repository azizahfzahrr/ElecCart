package com.azizahfzahrr.eleccart.data.repository

import com.azizahfzahrr.eleccart.data.local.entity.CartItem
import com.azizahfzahrr.eleccart.data.model.Address
import com.azizahfzahrr.eleccart.data.source.local.AddressDao
import com.azizahfzahrr.eleccart.data.source.local.AddressEntity
import javax.inject.Inject

class AddressRepository(private val addressDao: AddressDao) {
    suspend fun getAllAddress(): List<AddressEntity> = addressDao.getAddress()
    suspend fun insertAddress(address: AddressEntity) = addressDao.addAddress(address)
    suspend fun updateAddress(address: AddressEntity) = addressDao.updateAddress(address)
    suspend fun deleteAddress(address: AddressEntity) = addressDao.deleteAddress(address)

    suspend fun addSingleAddress(addressEntity: AddressEntity) {
        val existingItem = addressDao.getAddressItemById(addressEntity.id.toString())
        if (existingItem != null) {
            val updatedItem = existingItem.copy(
                addressRecipientName = addressEntity.addressRecipientName,
                addressRecipientPhone = addressEntity.addressRecipientPhone,
                addressRecipientFullAddress = addressEntity.addressRecipientFullAddress,
                addressRecipientProvince = addressEntity.addressRecipientProvince,
                addressRecipientPostalCode = addressEntity.addressRecipientPostalCode
            )
            addressDao.updateAddress(updatedItem)
        } else {
            addressDao.addAddress(addressEntity)
        }
    }
}