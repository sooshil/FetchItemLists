package com.sukajee.itemlist.presentation

import com.sukajee.itemlist.domain.model.Item

data class UiState(
    val itemsMap: Map<Int, List<Item>> = emptyMap(),
    val errorMessage: String? = null,
    val isLoading: Boolean = true
)