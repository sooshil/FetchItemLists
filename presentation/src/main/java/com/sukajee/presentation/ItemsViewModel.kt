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

    /**
     * A function that fetches data from api calling a function from repository.
     * Once the response is received, it will update the uiState
     * with the appropriate state variable.
     * */
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

    /**
     * A function to filter and sort the list of items.
     * This will not include the item having the name null or empty.
     * This will sort the list by listId and then name of a item.
     * This will return a map of Int (listId) to List<Item>
     * */
    private fun List<Item>.filteredData(): Map<Int, List<Item>> {
        return this
            .filterNot { it.name.isNullOrEmpty()}
            .sortedBy { it.name?.substringAfter(" ")?.toInt() }
            .sortedBy { it.listId }
            .groupBy { it.listId }
    }
}