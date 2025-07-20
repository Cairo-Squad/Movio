package com.cairosquad.remote.search

import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ResultResponse
import com.cairosquad.repository.utils.exception.EmptyResponseException
import com.cairosquad.repository.utils.exception.ServerException
import com.cairosquad.repository.utils.exception.UnauthorizedException
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
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
        // Given
        val page = 1
        val response = ResultResponse(
            page = 1,
            results = listOf(
                MovieRemoteDto(id = 1, title = "Movie 1", posterPath = null),
                MovieRemoteDto(id = null, title = "Movie 2", posterPath = null),
                null
            ),
            totalPages = 1,
            totalResults = 3
        )

        coEvery { apiService.getPersonalizedMovies(page) } returns Response.success(response)

        // When
        val result = dataSource.getPersonalizedMovies(page)

        // Then
        assertThat(result).hasSize(1)
        assertThat(result.first().id).isEqualTo(1)
    }

    @Test
    fun `getPersonalizedMovies should throw UnauthorizedException when response is 401`() = runTest {
        coEvery { apiService.getPersonalizedMovies(1) } returns Response.error(
            401,
            okhttp3.ResponseBody.create(null, """{ "status_message": "Unauthorized" }""")
        )

        var exception: Throwable? = null
        try {
            dataSource.getPersonalizedMovies(1)
        } catch (e: Throwable) {
            exception = e
        }

        assertThat(exception).isInstanceOf(UnauthorizedException::class.java)
        assertThat(exception?.message).isEqualTo("Unauthorized")
    }

    @Test
    fun `getPersonalizedMovies should throw EmptyResponseException when body is null`() = runTest {
        coEvery { apiService.getPersonalizedMovies(1) } returns Response.success(null)

        var exception: Throwable? = null
        try {
            dataSource.getPersonalizedMovies(1)
        } catch (e: Throwable) {
            exception = e
        }

        assertThat(exception).isInstanceOf(EmptyResponseException::class.java)
    }

    @Test
    fun `getSuggestedMovies should return valid list`() = runTest {
        val response = ResultResponse(
            page = 1,
            results = listOf(
                MovieRemoteDto(id = 3, title = "Suggested 1", posterPath = null),
                null,
                MovieRemoteDto(id = null, title = "Invalid", posterPath = null)
            ),
            totalPages = 1,
            totalResults = 3
        )

        coEvery { apiService.getSuggestedMovies() } returns Response.success(response)

        val result = dataSource.getSuggestedMovies()

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(3)
    }

    @Test
    fun `getSuggestedMovies should return empty list if all movies are null or invalid`() = runTest {
        val response = ResultResponse(
            page = 1,
            results = listOf(
                null,
                MovieRemoteDto(id = null, title = "Invalid", posterPath = null)
            ),
            totalPages = 1,
            totalResults = 2
        )

        coEvery { apiService.getSuggestedMovies() } returns Response.success(response)

        val result = dataSource.getSuggestedMovies()

        assertThat(result).isEmpty()
    }

    @Test
    fun `getSuggestedMovies should throw ServerException when API fails with 500`() = runTest {
        coEvery { apiService.getSuggestedMovies() } returns Response.error(
            500,
            okhttp3.ResponseBody.create(null, """{ "status_message": "Internal Error" }""")
        )

        var exception: Throwable? = null
        try {
            dataSource.getSuggestedMovies()
        } catch (e: Throwable) {
            exception = e
        }

        assertThat(exception).isInstanceOf(ServerException::class.java)
        assertThat(exception?.message).isEqualTo("Internal Error")
    }
}
