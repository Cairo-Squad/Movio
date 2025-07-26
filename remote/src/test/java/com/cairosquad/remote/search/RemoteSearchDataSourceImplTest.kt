package com.cairosquad.remote.search

import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ResultResponse
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.utils.exception.ServerException
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class RemoteSearchDataSourceImplTest {

    private lateinit var apiService: SearchApiService
    private lateinit var remoteSearchDataSource: RemoteSearchDataSourceImpl

    @Before
    fun setup() {
        apiService = mockk()
        remoteSearchDataSource = spyk(RemoteSearchDataSourceImpl(apiService))
    }

    @Test
    fun `should return movie list when getMovies is successful`() = runTest {
        val response = ResultResponse<MovieRemoteDto>(
            page = 1,
            results = listOf(
                MovieRemoteDto(id = 123, title = "Batman Begins", posterPath = "/batman.jpg", voteAverage = 8.2)
            ),
            totalPages = 1,
            totalResults = 1
        )
        coEvery { apiService.getMovies("batman", 1) } returns response

        val result = remoteSearchDataSource.getMovies("batman", 1)

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(123)
        assertThat(result[0].title).isEqualTo("Batman Begins")
    }

    @Test
    fun `should return series list when getSeries is successful`() = runTest {
        val response = ResultResponse<SeriesRemoteDto>(
            page = 1,
            results = listOf(
                SeriesRemoteDto(id = 456, name = "Batman Series", posterPath = "/batman_series.jpg")
            ),
            totalPages = 1,
            totalResults = 1
        )
        coEvery { apiService.getSeries("batman", 1) } returns response

        val result = remoteSearchDataSource.getSeries("batman", 1)

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(456)
        assertThat(result[0].name).isEqualTo("Batman Series")
    }

    @Test
    fun `should return artist list when getArtists is successful`() = runTest {
        val response = ResultResponse<ArtistRemoteDto>(
            page = 1,
            results = listOf(
                ArtistRemoteDto(id = 789, name = "Christian Bale", profilePath = "/bale.jpg")
            ),
            totalPages = 1,
            totalResults = 1
        )
        coEvery { apiService.getArtists("christian", 1) } returns response

        val result = remoteSearchDataSource.getArtists("christian", 1)

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(789)
        assertThat(result[0].name).isEqualTo("Christian Bale")
    }

    @Test
    fun `should throw EmptyResponseException when movie response body is null`() = runTest {
        val response = ResultResponse<MovieRemoteDto>(
            page = 1,
            results = null,
            totalPages = 1,
            totalResults = 0
        )
        coEvery { apiService.getMovies("query", 1) } returns response

        val result = remoteSearchDataSource.getMovies("query", 1)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw ServerException when API returns 500`() = runTest {
        val errorBody = """{ "status_message": "Internal Server Error" }"""
            .toResponseBody("application/json".toMediaTypeOrNull())

        val response = Response.error<ResultResponse<SeriesRemoteDto>>(500, errorBody)
        val exception = HttpException(response)

        coEvery { apiService.getSeries("query", 1) } throws exception

        var thrown: Throwable? = null
        try {
            remoteSearchDataSource.getSeries("query", 1)
        } catch (e: Throwable) {
            thrown = e
        }

        assertThat(thrown).isInstanceOf(ServerException::class.java)
        assertThat(thrown?.message).isEqualTo("Internal Server Error")
    }

    @Test
    fun `should return empty list when artist response has only null or invalid ids`() = runTest {
        val response = ResultResponse<ArtistRemoteDto>(
            page = 1,
            results = listOf(
                null,
                ArtistRemoteDto(id = null, name = "Invalid", profilePath = null)
            ),
            totalPages = 1,
            totalResults = 2
        )
        coEvery { apiService.getArtists("invalid", 1) } returns response

        val result = remoteSearchDataSource.getArtists("invalid", 1)

        assertThat(result).isEmpty()
    }
}
