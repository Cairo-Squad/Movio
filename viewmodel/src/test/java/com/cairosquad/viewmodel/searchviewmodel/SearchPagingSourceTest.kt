package com.cairosquad.viewmodel.searchviewmodel

import androidx.paging.PagingSource
import com.cairosquad.viewmodel.search.paging.SearchPagingSource
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchPagingSourceTest {

    private val testQuery = "query"

    @Test
    fun `should return Page when fetcher succeeds`() = runTest {
        //Given
        val expectedData = listOf("Item1", "Item2")
        val pagingSource = SearchPagingSource(
            query = testQuery,
            fetcher = { query, page ->
                assertEquals(testQuery, query)
                assertEquals(1, page)
                expectedData
            }
        )

        val params = PagingSource.LoadParams.Refresh(
            key = null as Int?,
            loadSize = 2,
            placeholdersEnabled = false
        )
        //When
        val result = pagingSource.load(params)
        //Then
        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(expectedData, page.data)
        assertEquals(null, page.prevKey)
        assertEquals(2, page.nextKey)
    }

    @Test
    fun `should return Error when fetcher throws exception`() = runTest {
        //Given
        val exception = RuntimeException("Failed to fetch")
        val pagingSource = SearchPagingSource<String>(
            query = testQuery,
            fetcher = { _, _ -> throw exception }
        )

        val params = PagingSource.LoadParams.Refresh(
            key = null as Int?,
            loadSize = 2,
            placeholdersEnabled = false
        )
        //When
        val result = pagingSource.load(params)
        //Then
        assertTrue(result is PagingSource.LoadResult.Error)
        val error = result as PagingSource.LoadResult.Error
        assertEquals(exception, error.throwable)
    }

    @Test
    fun `should return correct prevKey and nextKey when loading a non-first page`() = runTest {
        //Given
        val pagingSource = SearchPagingSource(
            query = testQuery,
            fetcher = { _, page -> listOf("Item$page") }
        )

        val params = PagingSource.LoadParams.Append(
            key = 3,
            loadSize = 1,
            placeholdersEnabled = false
        )
        //When
        val result = pagingSource.load(params)
        //Then
        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(2, page.prevKey)
        assertEquals(4, page.nextKey)
    }
}