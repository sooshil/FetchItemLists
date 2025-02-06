package com.sukajee.domain.data.repository

import com.sukajee.domain.data.model.Item
import com.sukajee.domain.service.ItemsService
import com.sukajee.domain.service.RetrofitInstance
import com.sukajee.domain.util.Result

interface ItemRepository {
    suspend fun getItems(): Result<List<Item>>
}

class ItemRepositoryImpl(
    private val api: ItemsService = RetrofitInstance.api
): ItemRepository {
    override suspend fun getItems(): Result<List<Item>> {
        return try {
            val response = api.getItems()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(data = it)
                } ?: Result.Error(message = "Response body was null.", throwable = null)
            } else {
                Result.Error(message = "Response was not successful.", throwable = null)
            }
        } catch (e: Exception) {
            Result.Error(message = e.message.toString(), throwable = e)
        }
    }
}