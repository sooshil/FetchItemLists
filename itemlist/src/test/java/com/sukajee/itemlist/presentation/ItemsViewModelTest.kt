package com.sukajee.itemlist.presentation

import com.sukajee.itemlist.domain.model.Item
import com.sukajee.itemlist.domain.repository.ItemsRepository
import com.sukajee.itemlist.domain.util.Result
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
    private val repository: ItemsRepository = mockk<ItemsRepository>()

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
                Item(itemId = 4, itemListId = 3, itemName = "Item 500")
            )
        )

        val viewModel = ItemsViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.itemsMap)
        assertEquals(1, state.itemsMap.size)
        assertEquals(3, state.itemsMap.keys.first())
        assertEquals("Item 500", state.itemsMap[3]?.get(0)?.itemName)
    }

    @Test
    fun `test viewmodel state updates correctly with error message when error response`() =
        runTest {

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
    fun `test viewmodel state updates correctly with exception message when error response`() =
        runTest {

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
    fun `test filterData function correctly filters when some itemNames are empty`() = runTest {

        coEvery { repository.getItems() } returns Result.Success(
            data = listOf(
                Item(itemId = 4, itemListId = 3, itemName = ""),
                Item(itemId = 44, itemListId = 2, itemName = "Item 300"),
                Item(itemId = 24, itemListId = 2, itemName = ""),
                Item(itemId = 34, itemListId = 1, itemName = ""),
                Item(itemId = 14, itemListId = 1, itemName = "Item 500"),
                Item(itemId = 40, itemListId = 3, itemName = "Item 600")
            )
        )

        val viewModel = ItemsViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.itemsMap)
        assertEquals(3, state.itemsMap.size)
        assertEquals(1, state.itemsMap.keys.first())
        assertEquals("Item 600", state.itemsMap[3]?.get(0)?.itemName)
    }

    @Test
    fun `test filterData function correctly filters when some itemNames are null`() = runTest {

        coEvery { repository.getItems() } returns Result.Success(
            data = listOf(
                Item(itemId = 4, itemListId = 3, itemName = null),
                Item(itemId = 44, itemListId = 2, itemName = "Item 300"),
                Item(itemId = 24, itemListId = 2, itemName = ""),
                Item(itemId = 34, itemListId = 1, itemName = null),
                Item(itemId = 14, itemListId = 1, itemName = null),
                Item(itemId = 40, itemListId = 3, itemName = "Item 600")
            )
        )

        val viewModel = ItemsViewModel(repository)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull(state.itemsMap)
        assertEquals(2, state.itemsMap.size)
        assertEquals(2, state.itemsMap.keys.first())
        assertEquals("Item 300", state.itemsMap[2]?.get(0)?.itemName)
    }
}