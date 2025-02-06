package com.sukajee.feature_item_domain.data.repository

import com.sukajee.feature_item_domain.data.model.Item
import com.sukajee.feature_item_domain.service.ItemsService
import com.sukajee.feature_item_domain.service.RetrofitInstance

interface ItemRepository {
    suspend fun getItems(): Result<List<Item>>
}

class ItemRepositoryImpl(
    val api: ItemsService = RetrofitInstance.api
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


sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String, val throwable: Throwable? = null): Result<Nothing>()
}

