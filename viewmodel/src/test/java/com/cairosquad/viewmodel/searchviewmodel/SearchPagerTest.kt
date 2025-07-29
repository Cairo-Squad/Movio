package com.cairosquad.viewmodel.searchviewmodel

import androidx.paging.testing.asSnapshot
import com.cairosquad.domain.usecase.ManageArtistUseCase
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.search.paging.SearchPager
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchPagerTest {

    private lateinit var manageSeriesUseCase: ManageSeriesUseCase
    private lateinit var manageMoviesUseCase: ManageMoviesUseCase
    private lateinit var manageArtistUseCase: ManageArtistUseCase
    private lateinit var searchPager: SearchPager

    private val testQuery = "batman"

    @Before
    fun setUp() {
        manageSeriesUseCase = mockk(relaxed = true)
        manageArtistUseCase = mockk(relaxed = true)
        manageMoviesUseCase = mockk(relaxed = true)
        searchPager = SearchPager(
            manageMoviesUseCase,
            manageSeriesUseCase,
            manageArtistUseCase
        )
    }

    @Test
    fun `movies should emit PagingData with correct movie items`() = runTest {
        // Given
        val expected = listOf(
            Movie(
                id = 1, title = "Batman Begins", rating = 0.1f, posterPath = "http:",
                genres = emptyList(),
                overview = "",
                releaseDate = 9L,
                runtimeMinutes = 5,
                trailerPath = ""
            ),
            Movie(
                id = 2, title = "The Dark Knight", rating = 0f, posterPath = "",
                genres = emptyList(),
                overview = "",
                releaseDate = 9L,
                runtimeMinutes = 5,
                trailerPath = ""
            )
        )
        coEvery { manageMoviesUseCase.getMoviesByQuery(testQuery, 1) } returns expected
        coEvery { manageMoviesUseCase.getMoviesByQuery(testQuery, match { it != 1 }) } returns emptyList()
        // When
        val flow = searchPager.movies(testQuery)
        val result = flow.asSnapshot()
        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `series should emit PagingData with correct series items`() = runTest {
        // Given
        val expected = listOf(
            Series(
                id = 1, title = "Batman Begins", rating = 0.1f, posterPath = "http:",
                trailerPath = "",
                genres = emptyList(),
                overview = "",
                releaseDate = 0L,
                seasonsCount = 1
            ),
            Series(
                id = 2, title = "The Dark Knight", rating = 0f, posterPath = "",
                trailerPath = "",
                genres = emptyList(),
                overview = "",
                releaseDate = 0L,
                seasonsCount = 1
            )
        )
        coEvery { manageSeriesUseCase.getSeriesByQuery(testQuery, 1) } returns expected
        coEvery { manageSeriesUseCase.getSeriesByQuery(testQuery, match { it != 1 }) } returns emptyList()
        // When
        val flow = searchPager.series(testQuery)
        val result = flow.asSnapshot()
        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `artists should emit PagingData with correct artist items`() = runTest {
        // Given
        val expected = listOf(
            Artist(1, "Christian Bale", ""),
            Artist(2, "Christopher Nolan", "")
        )
        coEvery { manageArtistUseCase.getArtistsByQuery(testQuery, 1) } returns expected
        coEvery { manageArtistUseCase.getArtistsByQuery(testQuery, match { it != 1 }) } returns emptyList()
        // When
        val flow = searchPager.artists(testQuery)
        val result = flow.asSnapshot()
        // Then
        assertEquals(expected, result)
    }
}
