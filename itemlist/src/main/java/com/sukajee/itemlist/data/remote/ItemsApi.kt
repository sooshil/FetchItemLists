package com.sukajee.itemlist.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface ItemsApi {

    @GET("hiring.json")
    suspend fun getItems(): Response<List<ItemDto>>
}