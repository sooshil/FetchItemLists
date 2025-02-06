package com.sukajee.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ItemsScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: ItemsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ItemsScreen(
        state = state,
        modifier = modifier
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemsScreen(
    state: UiState,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        state.itemsMap.forEach { (listId, items) ->
            stickyHeader {
                Text(text = listId.toString())
            }
            items(
                items = items,
                key = {
                    it.id
                }
            ) { item ->
                item.name?.let { name ->
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = name
                    )
                }
            }
        }
    }
}
