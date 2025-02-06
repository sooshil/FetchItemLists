package com.sukajee.domain.service

import com.sukajee.domain.data.model.Item
import retrofit2.Response
import retrofit2.http.GET

interface ItemsService {

    @GET("hiring.json")
    suspend fun getItems(): Response<List<Item>>
}