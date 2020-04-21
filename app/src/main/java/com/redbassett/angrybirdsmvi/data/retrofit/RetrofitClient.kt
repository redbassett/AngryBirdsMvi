package com.redbassett.angrybirdsmvi.data.retrofit

import com.redbassett.angrybirdsmvi.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val service by lazy {
        Retrofit.Builder()
            .baseUrl("https://angry-birds-api.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient().newBuilder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                    })
                    .build())
            .build()
            .create(BirdService::class.java)
    }
}
