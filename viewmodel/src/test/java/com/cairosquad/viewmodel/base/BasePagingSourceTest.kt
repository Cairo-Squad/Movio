package com.cairosquad.viewmodel.base

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BasePagingSourceTest {
    private val pagingSource = object : BasePagingSource<String>() {
        override suspend fun fetchData(page: Int): List<String> = emptyList()
    }

    class TestPagingSource(
        private val data: Map<Int, List<String>>,
        private val throwError: Boolean = false
    ) : BasePagingSource<String>() {
        override suspend fun fetchData(page: Int): List<String> {
            if (throwError) throw RuntimeException("Test error")
            return data[page] ?: emptyList()
        }
    }

    @Test
    fun `load should return LoadResult_Page with data`() = runTest {
        val data = mapOf(
            1 to listOf("A", "B", "C"),
            2 to listOf("D", "E")
        )
        val pagingSource = TestPagingSource(data)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 3,
                placeholdersEnabled = false
            )
        )

        val expected = PagingSource.LoadResult.Page(
            data = listOf("A", "B", "C"),
            prevKey = null,
            nextKey = 2
        )

        // Assertions
        assertTrue(result is PagingSource.LoadResult.Page)
        result as PagingSource.LoadResult.Page
        assertEquals(expected.data, result.data)
        assertEquals(expected.prevKey, result.prevKey)
        assertEquals(expected.nextKey, result.nextKey)
    }

    @Test
    fun `load should return LoadResult_Error when fetchData throws`() = runTest {
        val pagingSource = TestPagingSource(emptyMap(), throwError = true)

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 3,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Error)
    }
    @Test
    fun `getRefreshKey uses prevKey when nextKey is null`() {
        val page = PagingSource.LoadResult.Page(
            data = listOf("A"),
            prevKey = 3,
            nextKey = null
        )

        val state = PagingState(
            pages = listOf(page),
            anchorPosition = 0,
            config = PagingConfig(10),
            leadingPlaceholderCount = 0
        )

        val key = pagingSource.getRefreshKey(state)
        assertEquals(4, key) // 3 + 1
    }
    @Test
    fun `getRefreshKey should return correct key`() {
        val pagingSource = TestPagingSource(mapOf(1 to listOf("X")))
        val state = PagingState(
            pages = listOf(
                PagingSource.LoadResult.Page(
                    data = listOf("X"),
                    prevKey = null,
                    nextKey = 2
                )
            ),
            anchorPosition = 0,
            config = PagingConfig(pageSize = 1),
            leadingPlaceholderCount = 0
        )

        val key = pagingSource.getRefreshKey(state)

        assertEquals(1, key)
    }
    @Test
    fun `load returns page with null nextKey when items empty`() = runTest {
        val pagingSource = TestPagingSource(mapOf(1 to emptyList())
        )

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 3,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        result as PagingSource.LoadResult.Page
        assertEquals(null, result.nextKey)
        assertEquals(null, result.prevKey)
        assertTrue(result.data.isEmpty())
    }
    @Test
    fun `load returns correct prevKey for page 2`() = runTest {
        val pagingSource = TestPagingSource(mapOf(2 to listOf("X")))

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = 2,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        result as PagingSource.LoadResult.Page
        assertEquals(1, result.prevKey)
        assertEquals(3, result.nextKey)
    }
    @Test
    fun `getRefreshKey returns null when anchorPosition is null`() {
        val pagingSource = TestPagingSource(emptyMap())
        val state = PagingState<Int, String>(
            pages = emptyList(),
            anchorPosition = null,
            config = PagingConfig(10),
            leadingPlaceholderCount = 0
        )

        val key = pagingSource.getRefreshKey(state)
        assertNull(key)
    }
    @Test
    fun `getRefreshKey returns null when closestPageToPosition is null`() {
        val pagingSource = object : BasePagingSource<String>() {
            override suspend fun fetchData(page: Int): List<String> = emptyList()
            override fun getRefreshKey(state: PagingState<Int, String>): Int? = null // force null
        }

        val state = PagingState<Int, String>(
            pages = emptyList(),
            anchorPosition = 5,
            config = PagingConfig(10),
            leadingPlaceholderCount = 0
        )

        val key = pagingSource.getRefreshKey(state)
        assertNull(key)
    }
    @Test
    fun `getRefreshKey returns correct key from closest page`() {
        // Given
        val page1 = PagingSource.LoadResult.Page(
            data = listOf("A", "B"),
            prevKey = null,
            nextKey = 2
        )
        val page2 = PagingSource.LoadResult.Page(
            data = listOf("C", "D"),
            prevKey = 1,
            nextKey = 3
        )

        val state = PagingState(
            pages = listOf(page1, page2),
            anchorPosition = 2, // Index داخل الصفحة الثانية
            config = PagingConfig(pageSize = 2),
            leadingPlaceholderCount = 0
        )

        // When
        val refreshKey = pagingSource.getRefreshKey(state)

        // Then
        assertEquals(2, refreshKey) // لأن prevKey للصفحة هو 1 + 1 = 2
    }
    @Test
    fun `getRefreshKey uses nextKey when prevKey is null`() {
        val page = PagingSource.LoadResult.Page(
            data = listOf("A"),
            prevKey = null,
            nextKey = 5
        )
        val state = PagingState(
            pages = listOf(page),
            anchorPosition = 0,
            config = PagingConfig(10),
            leadingPlaceholderCount = 0
        )

        val key = pagingSource.getRefreshKey(state)
        assertEquals(4, key) // 5 - 1
    }
    @Test
    fun `getRefreshKey returns null when both prevKey and nextKey are null`() {
        val pagingSource = object : PagingSource<Int, String>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
                TODO("Not needed for this test")
            }

            override fun getRefreshKey(state: PagingState<Int, String>): Int? {
                val anchor = state.anchorPosition
                val anchorPage = state.closestPageToPosition(anchor ?: return null)
                return anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            }
        }

        val page: LoadResult.Page<Int, String> = LoadResult.Page(
            data = listOf("A"),
            prevKey = null,
            nextKey = null
        )

        val state = PagingState(
            pages = listOf(page),
            anchorPosition = 0,
            config = PagingConfig(10),
            leadingPlaceholderCount = 0
        )

        val key = pagingSource.getRefreshKey(state)
        assertNull(key)
    }

}