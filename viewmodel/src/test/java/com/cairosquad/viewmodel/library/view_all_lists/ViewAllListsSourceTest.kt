package com.cairosquad.viewmodel.library.view_all_lists

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ViewAllListsSourceTest {

    private lateinit var fetcher: suspend (Int) -> List<String>
    private lateinit var pagingSource: ViewAllListsSource<String>

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        fetcher = mockk()
        pagingSource = ViewAllListsSource(fetcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchData returns list from fetcher`() = runTest {
        val expectedList = listOf("Item1", "Item2")
        coEvery { fetcher(1) } returns expectedList

        val result = pagingSource.fetchData(page = 1, genreId = null)

        assertThat(result).isEqualTo(expectedList)
    }

    @Test
    fun `fetchData calls fetcher with correct page number`() = runTest {
        coEvery { fetcher(any()) } returns emptyList()

        pagingSource.fetchData(page = 5, genreId = null)

        coVerify(exactly = 1) { fetcher(5) }
    }

    @Test
    fun `fetchData works with multiple pages`() = runTest {
        coEvery { fetcher(1) } returns listOf("A")
        coEvery { fetcher(2) } returns listOf("B", "C")

        val page1 = pagingSource.fetchData(1, null)
        val page2 = pagingSource.fetchData(2, null)

        assertEquals(listOf("A"), page1)
        assertThat(page2).isEqualTo(listOf("B", "C"))
    }
}