package com.sukajee.presentation

import com.sukajee.domain.data.model.Item
import com.sukajee.domain.data.repository.ItemRepository
import com.sukajee.domain.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class ItemsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val repository: ItemRepository = mockk<ItemRepository>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test viewmodel state updates correctly with data when success response`() = runTest {

        coEvery { repository.getItems() } returns Result.Success(
            data = listOf(
                Item(id = 4, listId = 3, name = "Item 500")
            )
        )

        val viewModel = ItemsViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.itemsMap)
        assertEquals(1, state.itemsMap.size)
        assertEquals(3, state.itemsMap.keys.first())
        assertEquals("Item 500", state.itemsMap[3]?.get(0)?.name)
    }

    @Test
    fun `test viewmodel state updates correctly with error message when error response`() = runTest {

        coEvery { repository.getItems() } returns Result.Error(
            message = "Something went wrong."
        )

        val viewModel = ItemsViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertNotNull(state.errorMessage)
        assertEquals("Something went wrong.", state.errorMessage)
    }

    @Test
    fun `test viewmodel state updates correctly with exception message when error response`() = runTest {

        val errorMessage = "Something went wrong."
        val exception = IllegalArgumentException("Exception occurred")
        coEvery { repository.getItems() } returns Result.Error(
            throwable = exception,
            message = exception.message ?: errorMessage
        )

        val viewModel = ItemsViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertNotNull(state.errorMessage)
        assertEquals("Exception occurred", state.errorMessage)
    }

    @Test
    fun `test filterData function correctly filters when some names are empty`() = runTest {

        coEvery { repository.getItems() } returns Result.Success(
            data = listOf(
                Item(id = 4, listId = 3, name = ""),
                Item(id = 44, listId = 2, name = "Item 300"),
                Item(id = 24, listId = 2, name = ""),
                Item(id = 34, listId = 1, name = ""),
                Item(id = 14, listId = 1, name = "Item 500"),
                Item(id = 40, listId = 3, name = "Item 600")
            )
        )

        val viewModel = ItemsViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.itemsMap)
        assertEquals(3, state.itemsMap.size)
        assertEquals(1, state.itemsMap.keys.first())
        assertEquals("Item 600", state.itemsMap[3]?.get(0)?.name)
    }

    @Test
    fun `test filterData function correctly filters when some names are null`() = runTest {

        coEvery { repository.getItems() } returns Result.Success(
            data = listOf(
                Item(id = 4, listId = 3, name = null),
                Item(id = 44, listId = 2, name = "Item 300"),
                Item(id = 24, listId = 2, name = ""),
                Item(id = 34, listId = 1, name = null),
                Item(id = 14, listId = 1, name = null),
                Item(id = 40, listId = 3, name = "Item 600")
            )
        )

        val viewModel = ItemsViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.itemsMap)
        assertEquals(2, state.itemsMap.size)
        assertEquals(2, state.itemsMap.keys.first())
        assertEquals("Item 300", state.itemsMap[2]?.get(0)?.name)
    }
}