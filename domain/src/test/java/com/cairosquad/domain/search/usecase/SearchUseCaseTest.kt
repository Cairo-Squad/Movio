package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException

class SearchUseCaseTest {

    private lateinit var searchRepository: SearchRepository
    private lateinit var searchUseCase: SearchUseCase

    @BeforeEach
    fun setup() {

        searchRepository = mockk(relaxed = true)
        searchUseCase = SearchUseCase(searchRepository)
    }

    @Test
    fun `searchSeries should return list of series from repository`() = runTest {
        val query = "Breaking Bad"
        val expectedSeries = listOf(
            Series(1, "Breaking Bad", 9.5f, "/breaking_bad.jpg"),
            Series(2, "Better Call Saul", 9.0f, "/better_call_saul.jpg")
        )
        coEvery { searchRepository.searchSeries(query) } returns expectedSeries

        val result = searchUseCase.searchSeries(query)

        coVerify(exactly = 1) { searchRepository.searchSeries(query) }
        assertThat(result).isEqualTo(expectedSeries)
    }

    @Test
    fun `searchMovies should return list of Movies from repository`() = runTest {
        val query = "Inception"
        val expectedMovies = listOf(
            Movie(1, "Inception", 7.4f, "/Inception.jpg"),
            Movie(2, "TENET", 8.5f, "/TENET.jpg")
        )
        coEvery { searchRepository.searchMovies(query) } returns expectedMovies

        val result = searchUseCase.searchMovies(query)

        coVerify(exactly = 1) { searchRepository.searchMovies(query) }
        assertThat(result).isEqualTo(expectedMovies)
    }

    @Test
    fun `searchArtists should return list of artists from repository`() = runTest {
        val query = "Leonardo"

        val expectedArtists = listOf(
            Artist(1, "Leonardo DiCaprio", "/leo.jpg"),
            Artist(2, "Leonardo da Vinci", "/vinci.jpg")
        )
        coEvery { searchRepository.searchArtists(query) } returns expectedArtists

        val result = searchUseCase.searchArtists(query)

        coVerify(exactly = 1) { searchRepository.searchArtists(query) }
        assertThat(result).isEqualTo(expectedArtists)
    }

    @Test
    fun `searchSeries should return empty list when repository returns no results`() = runTest {
        val query = "Unknown Series"
        coEvery { searchRepository.searchSeries(query) } returns emptyList()

        val result = searchUseCase.searchSeries(query)

        assertThat(result).isEmpty()
        coVerify(exactly = 1) { searchRepository.searchSeries(query) }
    }

    @Test
    fun `searchMovies should return empty list when repository returns no results`() = runTest {
        val query = "Random Movie"
        coEvery { searchRepository.searchMovies(query) } returns emptyList()

        val result = searchUseCase.searchMovies(query)

        assertThat(result).isEmpty()
        coVerify(exactly = 1) { searchRepository.searchMovies(query) }
    }

    @Test
    fun `searchArtists should return empty list when repository returns no results`() = runTest {
        val query = "Unknown Artist"
        coEvery { searchRepository.searchArtists(query) } returns emptyList()

        val result = searchUseCase.searchArtists(query)

        assertThat(result).isEmpty()
        coVerify(exactly = 1) { searchRepository.searchArtists(query) }
    }

    @Test
    fun `searchSeries should throw exception when repository throws IOException`() = runTest {
        val query = "Breaking Bad"
        coEvery { searchRepository.searchSeries(query) } throws IOException("Network error")

        assertThrows<IOException> {
            searchUseCase.searchSeries(query)
        }
    }

    @Test
    fun `searchMovies should throw exception when repository throws RuntimeException`() = runTest {
        val query = "Inception"
        coEvery { searchRepository.searchMovies(query) } throws RuntimeException("Unexpected error")
        assertThrows<RuntimeException> {
            searchUseCase.searchMovies(query)
        }
    }

    @Test
    fun `searchArtists should throw exception when repository throws IllegalStateException`() = runTest {
            val query = "Leonardo"
            coEvery { searchRepository.searchArtists(query) } throws IllegalStateException("Invalid state")
            assertThrows<IllegalStateException> {
                searchUseCase.searchArtists(query)
            }
        }

    @Test
    fun `should not call any other repository methods`() = runTest {
        val query = "Test Query"
        coEvery { searchRepository.searchSeries(query) } returns emptyList()

        searchUseCase.searchSeries(query)

        // Verify ONLY searchSeries was called
        coVerify(exactly = 1) { searchRepository.searchSeries(query) }
        coVerify(exactly = 0) { searchRepository.searchMovies(any()) }
        coVerify(exactly = 0) { searchRepository.searchArtists(any()) }
    }
}