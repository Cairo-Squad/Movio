package com.cairosquad.remote.artists

import com.cairosquad.remote.artists.ArtistsRemoteDataSourceImpl.SeriesListResponse
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto
import com.google.common.truth.Truth.assertThat
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test

class ArtistsRemoteDataSourceImplTest {

    @Test
    fun `getArtist should return ArtistRemoteDto`() = runTest {
        val artistId = 42L
        val expected = ArtistRemoteDto(id = 42, name = "Jane Doe", profilePath = "/jane.jpg")

        val mockEngine = MockEngine { _ ->
            respond(
                content = Json.encodeToString(expected),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val dataSource = ArtistsRemoteDataSourceImpl(httpClient)

        val result = dataSource.getArtist(artistId)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getMoviesOfArtist should return list of MovieRemoteDto with non-null IDs`() = runTest {
        val artistId = 99L

        val mockResponse = ArtistsRemoteDataSourceImpl.MoviesListResponse(
            cast = listOf(
                MovieRemoteDto(id = 1, title = "Movie 1", posterPath = null),
                MovieRemoteDto(id = null, title = "Movie 2", posterPath = null)
            )
        )

        val mockEngine = MockEngine { _ ->
            respond(
                content = Json.encodeToString(mockResponse),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val dataSource = ArtistsRemoteDataSourceImpl(httpClient)

        val result = dataSource.getMoviesOfArtist(artistId)

        assertThat(result).hasSize(1)
        assertThat(result.first().id).isEqualTo(1)
    }

    @Test
    fun `getSeriesOfArtist should return list of SeriesRemoteDto with non-null IDs`() = runTest {
        val artistId = 7L

        val mockEngine = MockEngine { _ ->
            respond(
                content = Json.encodeToString(
                    SeriesListResponse(
                        cast = listOf(
                            SeriesRemoteDto(id = 100, name = "Series A", posterPath = "/a.jpg"),
                            SeriesRemoteDto(id = null, name = "Series B", posterPath = null)
                        )
                    )
                ),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val dataSource = ArtistsRemoteDataSourceImpl(httpClient)
        val result = dataSource.getSeriesOfArtist(artistId)

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(100)
    }

}