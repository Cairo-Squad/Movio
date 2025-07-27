package com.cairosquad.remote.search

import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.utils.exception.ServerException
import com.cairosquad.repository.utils.exception.UnauthorizedException
import com.cairosquad.repository.utils.sharedDto.remote.ResultResponse
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

class RemoteMovieDiscoveryDataSourceImplTest {

    private lateinit var apiService: SearchApiService
    private lateinit var dataSource: RemoteMovieDiscoveryDataSourceImpl

    @Before
    fun setup() {
        apiService = mockk()
        dataSource = spyk(RemoteMovieDiscoveryDataSourceImpl(apiService))
    }

    @Test
    fun `getPersonalizedMovies should return filtered non-null and valid movies`() = runTest {
        val page = 1
        val response = ResultResponse<MovieRemoteDto>(
            page = 1,
            results = listOf(
                MovieRemoteDto(id = 1, title = "Movie 1", posterPath = null),
                MovieRemoteDto(id = null, title = "Movie 2", posterPath = null),
                null
            ),
            totalPages = 1,
            totalResults = 3
        )

        coEvery { apiService.getPersonalizedMovies(page) } returns response

        val result = dataSource.getPersonalizedMovies(page)

        assertThat(result).hasSize(1)
        assertThat(result.first().id).isEqualTo(1)
    }

    @Test
    fun `getPersonalizedMovies should throw UnauthorizedException when response is 401`() = runTest {
        val page = 1
        val errorBody = """{ "status_message": "Unauthorized" }"""
            .toResponseBody("application/json".toMediaTypeOrNull())

        val response = Response.error<ResultResponse<MovieRemoteDto>>(401, errorBody)
        val exception = HttpException(response)

        coEvery { apiService.getPersonalizedMovies(page) } throws exception

        var thrown: Throwable? = null
        try {
            dataSource.getPersonalizedMovies(page)
        } catch (e: Throwable) {
            thrown = e
        }

        assertThat(thrown).isInstanceOf(UnauthorizedException::class.java)
        assertThat(thrown?.message).isEqualTo("Unauthorized")
    }

    @Test
    fun `getPersonalizedMovies should throw EmptyResponseException when body is null`() = runTest {
        val page = 1
        val response = ResultResponse<MovieRemoteDto>(
            page = 1,
            results = null,
            totalPages = 1,
            totalResults = 0
        )
        coEvery { apiService.getPersonalizedMovies(page) } returns response

        val result = dataSource.getPersonalizedMovies(page)

        assertThat(result).isEmpty()
    }

    @Test
    fun `getSuggestedMovies should return valid list`() = runTest {
        val response = ResultResponse<MovieRemoteDto>(
            page = 1,
            results = listOf(
                MovieRemoteDto(id = 3, title = "Suggested 1", posterPath = null),
                null,
                MovieRemoteDto(id = null, title = "Invalid", posterPath = null)
            ),
            totalPages = 1,
            totalResults = 3
        )

        coEvery { apiService.getSuggestedMovies() } returns response

        val result = dataSource.getSuggestedMovies()

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(3)
    }

    @Test
    fun `getSuggestedMovies should return empty list if all movies are null or invalid`() = runTest {
        val response = ResultResponse<MovieRemoteDto>(
            page = 1,
            results = listOf(
                null,
                MovieRemoteDto(id = null, title = "Invalid", posterPath = null)
            ),
            totalPages = 1,
            totalResults = 2
        )

        coEvery { apiService.getSuggestedMovies() } returns response

        val result = dataSource.getSuggestedMovies()

        assertThat(result).isEmpty()
    }

    @Test
    fun `getSuggestedMovies should throw ServerException when API fails with 500`() = runTest {
        val errorBody = """{ "status_message": "Internal Error" }"""
            .toResponseBody("application/json".toMediaTypeOrNull())

        val response = Response.error<ResultResponse<MovieRemoteDto>>(500, errorBody)
        val exception = HttpException(response)

        coEvery { apiService.getSuggestedMovies() } throws exception

        var thrown: Throwable? = null
        try {
            dataSource.getSuggestedMovies()
        } catch (e: Throwable) {
            thrown = e
        }

        assertThat(thrown).isInstanceOf(ServerException::class.java)
        assertThat(thrown?.message).isEqualTo("Internal Error")
    }
}
