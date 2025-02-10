package com.sukajee.itemlist.data.repository

import com.sukajee.itemlist.data.mappers.toItem
import com.sukajee.itemlist.data.remote.ItemsApi
import com.sukajee.itemlist.domain.model.Item
import com.sukajee.itemlist.domain.repository.ItemsRepository
import com.sukajee.itemlist.domain.util.Result

class ItemsRepositoryImpl(
    private val api: ItemsApi
): ItemsRepository {
    override suspend fun getItems(): Result<List<Item>> {
        return try {
            val response = api.getItems()
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(data = it.map { it.toItem() })
                } ?: Result.Error(message = "Response body was null.", throwable = null)
            } else {
                Result.Error(message = "Response was not successful.", throwable = null)
            }
        } catch (e: Exception) {
            Result.Error(message = e.message.toString(), throwable = e)
        }
    }
}