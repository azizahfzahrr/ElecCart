package com.azizahfzahrr.eleccart.data.source.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkConfig {
    private fun Interceptor(): Interceptor {
        return Interceptor {
            val chain = it.request()
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val token = "n..\$kYi[a4vWeZVhyDp3H4M|:*c<7]"
            val requestHeaders = chain.newBuilder()
                .addHeader("accept", "/")
                .addHeader("x-secret-app", token)
                .addHeader("x-user-id", "1")
                .build()
            it.proceed(requestHeaders)
        }
    }

    private fun getIntraCeptor(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(Interceptor()).build()
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://phincon-academy-api.onrender.com/phincon/api/")
            .client(getIntraCeptor())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApiService(): ApiService = getRetrofit().create(ApiService::class.java)

}