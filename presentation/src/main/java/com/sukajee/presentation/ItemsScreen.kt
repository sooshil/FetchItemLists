package com.sukajee.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sukajee.feature_item_android.R
import androidx.compose.material3.MaterialTheme.colorScheme as colors

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

    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        state.errorMessage?.let { message ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message,
                    style = typography.headlineMedium,
                    color = colors.onBackground
                )
            }
        }
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = rememberLazyListState()
        ) {
            state.itemsMap.forEach { (listId, items) ->
                stickyHeader {
                    HeaderRow(listId.toString())
                }
                val lastItem = items.lastOrNull()
                items(
                    items = items,
                    key = {
                        it.id
                    }
                ) { item ->
                    item.name?.let { name ->
                        NameRow(
                            name = name,
                            isLastName = name == lastItem?.name
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderRow(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = colors.tertiaryContainer)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.list_id, text),
            style = typography.headlineMedium,
            color = colors.onTertiaryContainer
        )
    }
}

@Composable
fun NameRow(
    name: String,
    isLastName: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = colors.secondaryContainer),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 12.dp,  vertical = 10.dp),
            text = name,
            style = typography.bodyLarge,
            color = colors.onSecondaryContainer
        )
        if (isLastName.not()) {
            HorizontalDivider(
                modifier = Modifier.align(Alignment.BottomCenter),
                color = colors.onSecondary.copy(alpha = 0.3f)
            )
        }
    }
}
