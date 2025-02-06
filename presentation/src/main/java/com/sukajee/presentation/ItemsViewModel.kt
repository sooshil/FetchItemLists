package com.sukajee.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sukajee.domain.data.model.Item
import com.sukajee.domain.data.repository.ItemRepository
import com.sukajee.domain.data.repository.ItemRepositoryImpl
import com.sukajee.domain.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ItemsViewModel(
    private val repository: ItemRepository = ItemRepositoryImpl()
): ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchItems()
    }

    private fun fetchItems() {
        viewModelScope.launch {
            when(val itemResponse = repository.getItems()) {
                is Result.Error -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = itemResponse.message,
                            isLoading = false,
                            itemsMap = emptyMap()
                        )
                    }
                }
                is Result.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = null,
                            isLoading = false,
                            itemsMap = itemResponse.data.filteredData()
                        )
                    }
                }
            }
        }
    }

    private fun List<Item>.filteredData(): Map<Int, List<Item>> {
        return this
            .filter { it.name.isNullOrEmpty().not()}
            .sortedBy { it.name }
            .sortedBy { it.listId }
            .groupBy { it.listId }
    }
}