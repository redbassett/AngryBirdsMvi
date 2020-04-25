package com.redbassett.angrybirdsmvi.data.retrofit

import com.redbassett.angrybirdsmvi.data.model.Bird
import retrofit2.http.GET
import retrofit2.http.Query

interface BirdService {
    @GET("/")
    suspend fun getBirds(@Query("page") page: Int = 1): List<Bird>
}
