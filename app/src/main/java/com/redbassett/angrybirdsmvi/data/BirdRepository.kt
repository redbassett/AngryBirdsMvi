package com.redbassett.angrybirdsmvi.data

import com.redbassett.angrybirdsmvi.data.retrofit.RetrofitClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BirdRepository @Inject constructor(private val client: RetrofitClient) {

    suspend fun getBirds(page: Int = 1) = client.service.getBirds(page)
}
