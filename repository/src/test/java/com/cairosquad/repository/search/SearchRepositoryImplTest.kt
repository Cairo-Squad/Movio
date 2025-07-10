package com.cairosquad.repository.search

import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.repository.search.dataSource.remote.search.RemoteSearchDataSource
import com.cairosquad.repository.search.dataSource.remote.search.dto.ArtistDto
import com.cairosquad.repository.search.dataSource.remote.search.dto.MovieDto
import com.cairosquad.repository.search.dataSource.remote.search.dto.SeriesDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@OptIn(ExperimentalCoroutinesApi::class)
class SearchRepositoryImplTest {

    private lateinit var remoteSearchDataSource: RemoteSearchDataSource
    private lateinit var searchRepository: SearchRepository

    @BeforeEach
    fun setup() {
        remoteSearchDataSource = mockk()
        searchRepository = SearchRepositoryImpl(remoteSearchDataSource)
    }

    @Test
    fun `searchSeries returns mapped series list on success`() = runTest {
        val query = "Breaking Bad"
        val dtoList = listOf(
            SeriesDto(id = 1L, name = "Breaking Bad", voteAverage = 9.5, posterPath = "/bb.jpg"),
            SeriesDto(
                id = 2L,
                name = "Better Call Saul",
                voteAverage = 8.8,
                posterPath = "/bcs.jpg"
            )
        )
        val expected = dtoList.map { it.toSeries() }

        coEvery { remoteSearchDataSource.searchSeries(query) } returns dtoList

        val result = searchRepository.searchSeries(query)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `searchSeries returns empty list when data source returns empty`() = runTest {
        val query = "Nonexistent Series"
        coEvery { remoteSearchDataSource.searchSeries(query) } returns emptyList()

        val result = searchRepository.searchSeries(query)

        assertThat(result).isEmpty()
    }

    @Test
    fun `searchSeries throws exception when data source throws`() = runTest {
        val query = "Error"
        coEvery { remoteSearchDataSource.searchSeries(query) } throws Exception("Network error")

        assertThrows<Exception> {
            remoteSearchDataSource.searchSeries(query)
        }
    }

    @Test
    fun `searchMovies returns mapped movies list on success`() = runTest {
        val query = "Inception"
        val dtoList = listOf(
            MovieDto(
                id = 10,
                title = "Inception",
                voteAverage = 8.8,
                posterPath = "/inception.jpg"
            ),
            MovieDto(
                id = 20,
                title = "Interstellar",
                voteAverage = 8.6,
                posterPath = "/interstellar.jpg"
            )
        )
        val expected = dtoList.map { it.toMovie() }

        coEvery { remoteSearchDataSource.searchMovies(query) } returns dtoList

        val result = searchRepository.searchMovies(query)

        assertThat(result).isEqualTo(expected)
        coVerify { remoteSearchDataSource.searchMovies(query) }
    }

    @Test
    fun `searchMovies returns empty list when data source returns empty`() = runTest {
        val query = "RandomMovie"
        coEvery { remoteSearchDataSource.searchMovies(query) } returns emptyList()

        val result = searchRepository.searchMovies(query)

        assertThat(result).isEmpty()
    }

    @Test
    fun `searchMovies throws exception when data source throws`() = runTest {
        val query = "Error"
        coEvery { remoteSearchDataSource.searchMovies(query) } throws Exception("API failure")

        assertThrows<Exception> {
            searchRepository.searchMovies(query)
        }
    }

    @Test
    fun `searchArtists returns mapped artists list on success`() = runTest {
        val query = "Eminem"
        val dtoList = listOf(
            ArtistDto(id = 100, name = "Eminem", profilePath = "/eminem.jpg"),
            ArtistDto(id = 101, name = "Dr. Dre", profilePath = "/dre.jpg")
        )
        val expected = dtoList.map { it.toArtist() }

        coEvery { remoteSearchDataSource.searchArtists(query) } returns dtoList

        val result = searchRepository.searchArtists(query)

        assertThat(result).isEqualTo(expected)
        coVerify { remoteSearchDataSource.searchArtists(query) }
    }

    @Test
    fun `searchArtists returns empty list when data source returns empty`() = runTest {
        val query = "Unknown Artist"
        coEvery { remoteSearchDataSource.searchArtists(query) } returns emptyList()

        val result = searchRepository.searchArtists(query)

        assertThat(result).isEmpty()
    }

    @Test
    fun `searchArtists throws exception when data source throws`() = runTest {
        val query = "Error"
        coEvery { remoteSearchDataSource.searchArtists(query) } throws Exception("Timeout")

        assertThrows<Exception> {
            searchRepository.searchArtists(query)
        }
    }
}