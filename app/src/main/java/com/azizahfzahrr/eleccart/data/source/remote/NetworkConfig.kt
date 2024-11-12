package com.azizahfzahrr.eleccart.data.source.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkConfig {
    private fun getInterceptor(): OkHttpClient{
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(logging).build()
    }
    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://fakestoreapi.in/api/")
            .client(getInterceptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun getApiService() = getRetrofit().create(ApiService::class.java)
}