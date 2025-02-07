package com.example.postapplication.data.api

import okhttp3.Dns
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)  // Increase connection timeout
        .readTimeout(30, TimeUnit.SECONDS)    // Increase read timeout
        .writeTimeout(30, TimeUnit.SECONDS)
        .dns(Dns.SYSTEM)
        .build()
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}