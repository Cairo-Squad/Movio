package com.cairosquad.viewmodel.foryou

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ForYouPagingSourceTest {

    private lateinit var pagingSource: ForYouPagingSource<Movie>
    private val mockFetcher: suspend (Int) -> List<Movie> = mockk()

    private val testMovie = Movie(
        id = 1L,
        title = "Test Movie",
        rating = 8.5f,
        posterPath = "/test.jpg",
        genres = listOf(Genre(id = 1, name = "Action")),
        overview = "Test overview",
        releaseDate = 1234567890L,
        runtimeMinutes = 120
    )

    @Before
    fun setUp() {
        pagingSource = ForYouPagingSource(mockFetcher)
    }

    @Test
    fun `load returns Page with data when fetcher succeeds`() = runTest {
        val testMovies = listOf(testMovie)
        coEvery { mockFetcher(1) } returns testMovies

        val params = PagingSource.LoadParams.Refresh(
            key = null as Int?,
            loadSize = 2,
            placeholdersEnabled = false
        )
        val result = pagingSource.load(params)

        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(testMovies, page.data)
        assertNull(page.prevKey)
        assertEquals(2, page.nextKey)
    }

    @Test
    fun `load returns empty Page when fetcher returns empty list`() = runTest {
        coEvery { mockFetcher(1) } returns emptyList()

        val params = PagingSource.LoadParams.Refresh(
            key = null as Int?,
            loadSize = 2,
            placeholdersEnabled = false
        )
        val result = pagingSource.load(params)
        
        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertTrue(page.data.isEmpty())
        assertNull(page.prevKey)
        assertNull(page.nextKey)
    }

    @Test
    fun `load returns Error when fetcher throws exception`() = runTest {
        val testException = RuntimeException("Network error")
        coEvery { mockFetcher(1) } throws testException

        val params = PagingSource.LoadParams.Refresh(
            key = null as Int?,
            loadSize = 2,
            placeholdersEnabled = false
        )
        val result = pagingSource.load(params)

        assertTrue(result is PagingSource.LoadResult.Error)
        assertEquals(testException, (result as PagingSource.LoadResult.Error).throwable)
    }

    @Test
    fun `getRefreshKey returns middle page when anchor position is set`() = runTest {
        val pages = List(5) { page ->
            PagingSource.LoadResult.Page(
                data = List(20) { testMovie.copy(id = it.toLong() + page * 20L) },
                prevKey = if (page == 0) null else page,
                nextKey = if (page == 4) null else page + 2
            )
        }

        val state = PagingState(
            pages = pages,
            anchorPosition = 50,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        val refreshKey = pagingSource.getRefreshKey(state)

        assertEquals(3, refreshKey)
    }
}