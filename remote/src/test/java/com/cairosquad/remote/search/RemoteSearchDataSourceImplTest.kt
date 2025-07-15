package com.cairosquad.remote.search

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
import org.junit.Before
import org.junit.Test

class RemoteSearchDataSourceImplTest {
    private lateinit var mockEngine: MockEngine
    private lateinit var httpClient: HttpClient
    private lateinit var remoteSearchDataSource: RemoteSearchDataSourceImpl

    @Before
    fun setup() {
        mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/3/search/movie" -> {
                    respond(
                        content = """
                        {
                          "page": 1,
                          "results": [
                            {
                              "id": 123,
                              "title": "Batman Begins",
                              "poster_path": "/batman.jpg",
                              "release_date": "2005-06-15",
                              "vote_average": 8.2
                            }
                          ],
                          "total_pages": 1,
                          "total_results": 1
                        }
                    """.trimIndent(),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/3/search/tv" -> {
                    respond(
                        content = """
                        {
                          "page": 1,
                          "results": [
                            {
                              "id": 456,
                              "name": "Batman Series",
                              "poster_path": "/batman_series.jpg",
                              "first_air_date": "2008-01-01",
                              "vote_average": 7.5
                            }
                          ],
                          "total_pages": 1,
                          "total_results": 1
                        }
                    """.trimIndent(),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/3/search/person" -> {
                    respond(
                        content = """
                        {
                          "page": 1,
                          "results": [
                            {
                              "id": 789,
                              "name": "Christian Bale",
                              "profile_path": "/bale.jpg"
                            }
                          ],
                          "total_pages": 1,
                          "total_results": 1
                        }
                    """.trimIndent(),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                else -> error("Unhandled path: ${request.url.encodedPath}")
            }
        }

        httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        remoteSearchDataSource = RemoteSearchDataSourceImpl(httpClient)
    }

    @Test
    fun `should return movie list when getMovies is called with valid query`() = runTest {
        // Given
        val query = "batman"

        // When
        val result = remoteSearchDataSource.getMovies(query)

        // Then
        assertThat(result).isNotEmpty()
        assertThat(result[0].id).isEqualTo(123)
        assertThat(result[0].title).isEqualTo("Batman Begins")
    }

    @Test
    fun `should return series list when getSeries is called with valid query`() = runTest {
        // Given
        val query = "batman"

        // When
        val result = remoteSearchDataSource.getSeries(query)

        // Then
        assertThat(result).isNotEmpty()
        assertThat(result[0].id).isEqualTo(456)
        assertThat(result[0].name).isEqualTo("Batman Series")
    }

    @Test
    fun `should return artist list when getArtists is called with valid query`() = runTest {
        // Given
        val query = "christian"

        // When
        val result = remoteSearchDataSource.getArtists(query)

        // Then
        assertThat(result).isNotEmpty()
        assertThat(result[0].id).isEqualTo(789)
        assertThat(result[0].name).isEqualTo("Christian Bale")
    }

}