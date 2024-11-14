package com.azizahfzahrr.eleccart.di

import android.content.Context
import androidx.room.Room
import com.azizahfzahrr.eleccart.data.repository.CartRepository
import com.azizahfzahrr.eleccart.data.repository.ProductRepository
import com.azizahfzahrr.eleccart.data.repository.ProductRepositoryImpl
import com.azizahfzahrr.eleccart.data.repository.WishlistRepository
import com.azizahfzahrr.eleccart.data.source.local.CartDao
import com.azizahfzahrr.eleccart.data.source.local.CartDatabase
import com.azizahfzahrr.eleccart.data.source.local.WishlistDao
import com.azizahfzahrr.eleccart.data.source.local.WishlistDatabase
import com.azizahfzahrr.eleccart.data.source.remote.ApiService
import com.azizahfzahrr.eleccart.data.source.remote.NetworkConfig
import com.azizahfzahrr.eleccart.data.source.remote.RemoteDataSource
import com.azizahfzahrr.eleccart.data.source.remote.RemoteDataSourceImpl
import com.azizahfzahrr.eleccart.domain.usecase.ProductUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return NetworkConfig().getApiService()
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: ApiService): RemoteDataSource {
        return RemoteDataSourceImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideProductRepository(remoteDataSource: RemoteDataSource): ProductRepository {
        return ProductRepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideProductUseCase(productRepository: ProductRepository): ProductUseCase {
        return ProductUseCase(productRepository)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CartDatabase {
        return Room.databaseBuilder(context, CartDatabase::class.java, "cart_database").build()
    }

    @Provides
    fun provideCartDao(cartDatabase: CartDatabase): CartDao {
        return cartDatabase.cartDao()
    }

    @Provides
    fun provideCartRepository(cartDao: CartDao): CartRepository {
        return CartRepository(cartDao)
    }

    @Provides
    @Singleton
    fun provideWishlistDatabase(@ApplicationContext context: Context): WishlistDatabase {
        return Room.databaseBuilder(context, WishlistDatabase::class.java, "wishlist_database").build()
    }

    @Provides
    fun provideWishlistDao(wishlistDatabase: WishlistDatabase): WishlistDao {
        return wishlistDatabase.wishlistDao()
    }

    @Provides
    fun provideWishlistRepository(wishlistDao: WishlistDao): WishlistRepository {
        return WishlistRepository(wishlistDao)
    }
}