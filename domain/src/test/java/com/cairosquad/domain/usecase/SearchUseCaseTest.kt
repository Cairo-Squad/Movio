package com.cairosquad.domain.usecase

import com.cairosquad.domain.search.repository.SearchHistoryRepository
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

class SearchUseCaseTest {

    private val searchRepository = mockk<SearchRepository>()
    private val recentSearchRepository = mockk<SearchHistoryRepository>(relaxed = true)
    private lateinit var useCase: SearchUseCase

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        useCase = SearchUseCase(searchRepository, recentSearchRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should return movies and save query when getMovies is called`() = runTest {
        // Given
        val query = "Batman"
        val movies = listOf(
            Movie(id = 1, title = "Batman Begins", rating = 8.2f, posterPath = "/batman1.jpg"),
            Movie(id = 2, title = "The Dark Knight", rating = 9.0f, posterPath = "/batman2.jpg")
        )

        coEvery { searchRepository.getMovies(query) } returns movies
        coEvery { recentSearchRepository.addQuery(query) } just runs

        val result = useCase.getMovies(query)

        coVerify { searchRepository.getMovies(query) }
        coVerify { recentSearchRepository.addQuery(query) }
    }

    @Test
    fun `should return series and save query when getSeries is called`() = runTest {
        // Given
        val query = "Breaking Bad"
        val series = listOf(
            Series(id = 1, title = "Breaking Bad", rating = 9.5f, posterPath = "/bb.jpg")
        )

        coEvery { searchRepository.getSeries(query) } returns series
        coEvery { recentSearchRepository.addQuery(query) } just runs

        val result = useCase.getSeries(query)

        coVerify { searchRepository.getSeries(query) }
        coVerify { recentSearchRepository.addQuery(query) }
    }

    @Test
    fun `should return artists and save query when getArtists is called`() = runTest {
        // Given
        val query = "Leonardo"
        val artists = listOf(
            Artist(
                id = 1, name = "Leonardo DiCaprio", photoPath = "/leo.jpg",
            )
        )

        coEvery { searchRepository.getArtists(query) } returns artists
        coEvery { recentSearchRepository.addQuery(query) } just runs

        val result = useCase.getArtists(query)

        coVerify { searchRepository.getArtists(query) }
        coVerify { recentSearchRepository.addQuery(query) }
    }
}
