package com.cairosquad.viewmodel.searchviewmodel

import androidx.paging.testing.asSnapshot
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

    private lateinit var searchUseCase: SearchUseCase
    private lateinit var searchPager: SearchPager

    private val testQuery = "batman"

    @Before
    fun setUp() {
        searchUseCase = mockk()
        searchPager = SearchPager(searchUseCase)
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
                id = 2, title = "The Dark Knight", rating = 0.0f, posterPath = "",
                genres = emptyList(),
                overview = "",
                releaseDate = 9L,
                runtimeMinutes = 5,
                trailerPath = ""
            )
        )
        coEvery { searchUseCase.getMovies(testQuery, 1) } returns expected
        coEvery { searchUseCase.getMovies(testQuery, match { it != 1 }) } returns emptyList()
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
                id = 2, title = "The Dark Knight", rating = 0.0f, posterPath = "",
                trailerPath = "",
                genres = emptyList(),
                overview = "",
                releaseDate = 0L,
                seasonsCount = 1
            )
        )
        coEvery { searchUseCase.getSeries(testQuery, 1) } returns expected
        coEvery { searchUseCase.getSeries(testQuery, match { it != 1 }) } returns emptyList()
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
        coEvery { searchUseCase.getArtists(testQuery, 1) } returns expected
        coEvery { searchUseCase.getArtists(testQuery, match { it != 1 }) } returns emptyList()
        // When
        val flow = searchPager.artists(testQuery)
        val result = flow.asSnapshot()
        // Then
        assertEquals(expected, result)
    }
}
