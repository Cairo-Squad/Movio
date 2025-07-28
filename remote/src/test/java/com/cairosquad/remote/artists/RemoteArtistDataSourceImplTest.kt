package com.cairosquad.remote.artists

import com.cairosquad.repository.artists.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.CreditResponse
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
        coEvery { apiService.getArtistById(artistId) } returns expectedArtist

        // When
        val result = dataSource.getArtistById(artistId)

        // Then
        assertThat(result).isEqualTo(expectedArtist)
    }

    @Test
    fun `getMovieTopCast should return cast list`() = runTest {
        val page = 1
        val movieId = 10L
        val expected = CreditResponse(
            id = 10, cast = listOf(
                ArtistRemoteDto(id = 1, name = "Cillian Murphy", profilePath = null),
                ArtistRemoteDto(id = 2, name = "Emily Blunt", profilePath = null)
            )
        )
        coEvery { apiService.getMovieTopCast(movieId, page) } returns expected

        val result = dataSource.getMovieTopCast(movieId, page)

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Cillian Murphy")
        assertThat(result[1].name).isEqualTo("Emily Blunt")
    }


    @Test
    fun `getMovieTopCast filters out null items in cast list`() = runTest {

        val artist1 = ArtistRemoteDto(id = 1, name = "Actor A")
        val artist2 = ArtistRemoteDto(id = 2, name = "Actor B")
        val response = CreditResponse(cast = listOf(artist1, null, artist2), id = 1)
        coEvery { apiService.getMovieTopCast(1L, 1) } returns response

        val result = dataSource.getMovieTopCast(1L, 1)

        assertThat(result).containsExactly(artist1, artist2)
    }

    @Test
    fun `getMovieTopCast returns empty list when cast is null`() = runTest {

        val response = CreditResponse(cast = null, id = 2)
        coEvery { apiService.getMovieTopCast(1L, 1) } returns response

        val result = dataSource.getMovieTopCast(1L, 1)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return filtered cast list when getSeriesTopCast is successful`() = runTest {
        val seriesId = 321L
        val response = CreditResponse(
            id = 12345,
            cast = listOf(
                ArtistRemoteDto(
                    id = 201,
                    name = "Bryan Cranston",
                    profilePath = "/bryan.jpg",
                    placeOfBirth = "Hollywood, California",
                    birthday = "1956-03-07",
                    biography = "American actor and producer",
                    department = "Acting"
                ),
                null,
                ArtistRemoteDto(
                    id = 202,
                    name = "Aaron Paul",
                    profilePath = "/aaron.jpg",
                    placeOfBirth = "Emmett, Idaho",
                    birthday = "1979-08-27",
                    biography = "American actor",
                    department = "Acting"
                )
            )
        )
        coEvery { apiService.getSeriesTopCast(seriesId, 1) } returns response

        val result = dataSource.getSeriesTopCast(seriesId, 1)

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Bryan Cranston")
        assertThat(result[1].name).isEqualTo("Aaron Paul")
    }

    @Test
    fun `should return empty list when cast response is null`() = runTest {
        val seriesId = 555L
        val response = CreditResponse(id = 12345, cast = null)
        coEvery { apiService.getSeriesTopCast(seriesId, 1) } returns response

        val result = dataSource.getSeriesTopCast(seriesId, 1)

        assertThat(result).isEmpty()
    }


    private companion object {
        val expectedArtist = ArtistRemoteDto(id = 42, name = "Jane Doe", profilePath = "/jane.jpg")
    }
}