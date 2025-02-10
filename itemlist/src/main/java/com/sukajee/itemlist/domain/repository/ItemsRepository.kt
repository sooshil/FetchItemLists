package com.sukajee.itemlist.domain.repository

import com.sukajee.itemlist.domain.model.Item
import com.sukajee.itemlist.domain.util.Result

interface ItemsRepository {
    suspend fun getItems(): Result<List<Item>>
}