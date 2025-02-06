package com.sukajee.presentation

import com.sukajee.domain.data.model.Item

data class UiState(
    val itemsMap: Map<Int, List<Item>> = emptyMap(),
    val errorMessage: String? = null,
    val isLoading: Boolean = true
)