package com.sukajee.feature_item_android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sukajee.feature_item_domain.data.repository.ItemRepository
import com.sukajee.feature_item_domain.data.repository.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ItemsViewModel(
    private val repository: ItemRepository
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
                            people = emptyList()
                        )
                    }
                }
                is Result.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = null,
                            isLoading = false,
                            people = itemResponse.data
                        )
                    }
                }
            }
        }
    }

}