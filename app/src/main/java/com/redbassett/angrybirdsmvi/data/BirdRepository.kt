package com.redbassett.angrybirdsmvi.data

import com.redbassett.angrybirdsmvi.data.retrofit.RetrofitClient

object BirdRepository {
    private val client = RetrofitClient.service

    suspend fun getBirds(page: Int = 1) = client.getBirds(page)
}
