package com.azizahfzahrr.eleccart.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WishlistDao {

    @Insert
    suspend fun addProductToWishlist(product: WishlistEntity)

    @Query("SELECT * FROM wishlist_database")
    fun getAllWishlistProducts(): List<WishlistEntity>

    @Delete
    suspend fun removeProductFromWishlist(product: WishlistEntity)
}
