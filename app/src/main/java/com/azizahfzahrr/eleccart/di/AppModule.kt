package com.azizahfzahrr.eleccart.di

import com.azizahfzahrr.eleccart.data.repository.ProductRepository
import com.azizahfzahrr.eleccart.data.repository.ProductRepositoryImpl
import com.azizahfzahrr.eleccart.data.source.remote.ApiService
import com.azizahfzahrr.eleccart.data.source.remote.NetworkConfig
import com.azizahfzahrr.eleccart.data.source.remote.RemoteDataSource
import com.azizahfzahrr.eleccart.data.source.remote.RemoteDataSourceImpl
import com.azizahfzahrr.eleccart.domain.usecase.ProductUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}