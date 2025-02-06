package com.sukajee.presentation

import com.sukajee.domain.data.model.Item

data class UiState(
    val items: List<Item> = emptyList(),
    val errorMessage: String? = null,
    val isLoading: Boolean = true
)