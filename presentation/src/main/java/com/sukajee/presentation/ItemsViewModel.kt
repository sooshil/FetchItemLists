package com.sukajee.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sukajee.domain.data.repository.ItemRepository
import com.sukajee.domain.data.repository.ItemRepositoryImpl
import com.sukajee.domain.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ItemsViewModel(
    private val repository: ItemRepository = ItemRepositoryImpl(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchItems()
    }

    private fun fetchItems() {
        viewModelScope.launch(dispatcher) {
            when(val itemResponse = repository.getItems()) {
                is Result.Error -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = itemResponse.message,
                            isLoading = false,
                            items = emptyList()
                        )
                    }
                }
                is Result.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            errorMessage = null,
                            isLoading = false,
                            items = itemResponse.data
                        )
                    }
                }
            }
        }
    }

}