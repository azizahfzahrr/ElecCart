package com.azizahfzahrr.eleccart.data.source.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkConfig {
    private fun getInterceptor(): Interceptor {
        return Interceptor { chain ->
            val token = "n..\$kYi[a4vWeZVhyDp3H4M|:*c<7]"
            val originalRequest = chain.request()
            val request = originalRequest.newBuilder()
                .addHeader("accept", "*/*")
                .addHeader("x-secret-app", token)
                .addHeader("x-user-id", "1")
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(request)
        }
    }

    private fun getOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(getInterceptor())
            .addInterceptor(logging) // Add logging interceptor for debugging
            .build()
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://phincon-academy-api.onrender.com/phincon/api/")
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApiService(): ApiService = getRetrofit().create(ApiService::class.java)

}