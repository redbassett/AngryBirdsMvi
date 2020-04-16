package com.redbassett.angrybirdsmvi.data

import com.redbassett.angrybirdsmvi.data.model.Bird
import com.redbassett.angrybirdsmvi.data.retrofit.RetrofitClient

object BirdRepository {
    private val client = RetrofitClient.service

    suspend fun getAllBirds(): List<Bird> {
        return client.getAllBirds()
    }
}
