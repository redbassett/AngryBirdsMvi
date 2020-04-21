package com.redbassett.angrybirdsmvi.data.retrofit

import com.redbassett.angrybirdsmvi.data.model.Bird
import retrofit2.http.GET

interface BirdService {
    @GET("/")
    suspend fun getAllBirds(): List<Bird>
}
