package com.azizahfzahrr.eleccart.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.azizahfzahrr.eleccart.data.local.entity.CartItem

@Dao
interface AddressDao {

    @Insert
    suspend fun addAddress(addressEntity: AddressEntity)

    @Query("SELECT * FROM address_entity")
    suspend fun getAddress(): List<AddressEntity>

    @Update
    suspend fun updateAddress(addressEntity: AddressEntity)

    @Delete
    suspend fun deleteAddress(addressEntity: AddressEntity)

    @Query("SELECT * FROM address_entity WHERE id = :productId")
    suspend fun getAddressItemById(productId: String): AddressEntity?
}