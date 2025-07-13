package com.cairosquad.domain.usecase

import com.cairosquad.domain.search.repository.RecentSearchRepository
import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.domain.search.usecase.SearchUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class SearchUseCaseTest {

    private val searchRepository = mockk<SearchRepository>()
    private val recentSearchRepository = mockk<RecentSearchRepository>(relaxed = true)
    private lateinit var searchUseCase: SearchUseCase

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        searchUseCase = SearchUseCase(searchRepository, recentSearchRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return movies and save query when getMovies is called`() = runTest {
        // Given
        val query = "Batman"
        val expectedMovies = listOf(
            Movie(id = 1, title = "Batman Begins", rating = 8.2f, posterPath = "/batman1.jpg"),
            Movie(id = 2, title = "The Dark Knight", rating = 9.0f, posterPath = "/batman2.jpg")
        )
        coEvery { searchRepository.getMovies(query) } returns expectedMovies
        coEvery { recentSearchRepository.addQuery(query) } just runs

        // When
        val result = searchUseCase.getMovies(query)

        // Then
        assertEquals(expectedMovies, result)
        coVerify { searchRepository.getMovies(query) }
        coVerify { recentSearchRepository.addQuery(query) }
    }

    @Test
    fun `should return series and save query when getSeries is called`() = runTest {
        // Given
        val query = "Breaking Bad"
        val expectedSeries = listOf(
            Series(id = 1, title = "Breaking Bad", rating = 9.5f, posterPath = "/bb.jpg")
        )
        coEvery { searchRepository.getSeries(query) } returns expectedSeries
        coEvery { recentSearchRepository.addQuery(query) } just runs

        // When
        val result = searchUseCase.getSeries(query)

        // Then
        assertEquals(expectedSeries, result)
        coVerify { searchRepository.getSeries(query) }
        coVerify { recentSearchRepository.addQuery(query) }
    }

    @Test
    fun `should return artists and save query when getArtists is called`() = runTest {
        // Given
        val query = "Leonardo"
        val expectedArtists = listOf(
            Artist(id = 1, name = "Leonardo DiCapri", photoPath = "/leo.jpg")
        )
        coEvery { searchRepository.getArtists(query) } returns expectedArtists
        coEvery { recentSearchRepository.addQuery(query) } just runs

        // When
        val result = searchUseCase.getArtists(query)

        // Then
        assertEquals(expectedArtists, result)
        coVerify { searchRepository.getArtists(query) }
        coVerify { recentSearchRepository.addQuery(query) }
    }
}
