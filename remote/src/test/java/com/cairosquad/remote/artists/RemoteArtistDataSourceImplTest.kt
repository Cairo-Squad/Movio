package com.cairosquad.remote.artists

import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RemoteArtistDataSourceImplTest {

    private lateinit var apiService: ArtistsApiService
    private lateinit var dataSource: RemoteArtistDataSourceImpl

    @Before
    fun setup() {
        apiService = mockk(relaxed = true)
        dataSource = spyk(RemoteArtistDataSourceImpl(apiService))
    }

    @Test
    fun `getArtist should return artist data`() = runTest {
        // Given
        val artistId = 42L
        coEvery { apiService.getArtist(artistId) } returns expectedArtist

        // When
        val result = dataSource.getArtist(artistId)

        // Then
        assertThat(result).isEqualTo(expectedArtist)
    }

    @Test
    fun `getMoviesOfArtist should return list with non-null IDs`() = runTest {
        // Given
        val artistId = 99L
        val response = MoviesListResponse(
            movies = listOf(remoteMovies)
        )

        coEvery { apiService.getMoviesOfArtist(artistId) } returns response

        // When
        val result = dataSource.getMoviesOfArtist(artistId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result.first().id).isEqualTo(1)
    }

    @Test
    fun `getSeriesOfArtist should return list with non-null IDs`() = runTest {
        // Given
        val artistId = 7L
        val response = SeriesListResponse(
            series = listOf(remoteSeries)
        )

        coEvery { apiService.getSeriesOfArtist(artistId) } returns response

        // When
        val result = dataSource.getSeriesOfArtist(artistId)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(100)
    }

    private companion object {
        val remoteMovies = MovieRemoteDto(id = 1, title = "Movie 1", posterPath = null)
        val remoteSeries = SeriesRemoteDto(id = 100, name = "Series A", posterPath = "/a.jpg")
        val expectedArtist = ArtistRemoteDto(id = 42, name = "Jane Doe", profilePath = "/jane.jpg")
    }
}