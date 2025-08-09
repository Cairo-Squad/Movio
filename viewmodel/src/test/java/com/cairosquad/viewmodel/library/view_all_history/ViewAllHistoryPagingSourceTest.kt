package com.cairosquad.viewmodel.library.view_all_history

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ViewAllHistoryPagingSourceTest {
    @Test
    fun `fetchData should return data from fetcher`() = runTest {
        val expectedList = listOf("Item1", "Item2")
        val pagingSource = ViewAllHistoryPagingSource<String> { page ->
            assertThat(page).isEqualTo(1) // verify page argument
            expectedList
        }

        val result = pagingSource.fetchData(page = 1, genreId = null)

        assertThat(result).isEqualTo(expectedList)
    }

    @Test
    fun `fetchData should return empty list when fetcher returns nothing`() = runTest {
        val pagingSource = ViewAllHistoryPagingSource<String> { _ -> emptyList() }

        val result = pagingSource.fetchData(page = 5, genreId = null)

        assertThat(result).isEmpty()
    }

    @Test
    fun `fetchData should pass correct page to fetcher`() = runTest {
        var receivedPage: Int? = null
        val pagingSource = ViewAllHistoryPagingSource<String> { page ->
            receivedPage = page
            emptyList()
        }

        pagingSource.fetchData(page = 10, genreId = null)

        assertThat(receivedPage).isEqualTo(10)
    }
}