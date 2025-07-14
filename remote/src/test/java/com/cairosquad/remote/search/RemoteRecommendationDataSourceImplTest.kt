package com.cairosquad.remote.search

import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.SearchResultResponse
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

class RemoteRecommendationDataSourceImplTest {

    private lateinit var mockEngine: MockEngine
    private lateinit var httpClient: HttpClient
    private lateinit var remoteDataSource: RemoteMovieDiscoveryDataSourceImpl

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    @Before
    fun setup() {
        mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/3/movie/top_rated" -> {
                    respond(
                        content = json.encodeToString(
                            SearchResultResponse(
                                page = 1,
                                results = listOf(
                                    MovieRemoteDto(
                                        id = 1,
                                        title = "Movie 1",
                                        posterPath = null,
                                        voteAverage = null
                                    ),
                                    MovieRemoteDto(
                                        id = 2,
                                        title = "Movie 2",
                                        posterPath = null,
                                        voteAverage = null
                                    )
                                ),
                                totalPages = 1,
                                totalResults = 2
                            )
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                "/3/movie/now_playing" -> {
                    respond(
                        content = json.encodeToString(
                            SearchResultResponse(
                                page = 1,
                                results = listOf(
                                    MovieRemoteDto(
                                        id = 3,
                                        title = "Movie 3",
                                        posterPath = null,
                                        voteAverage = null
                                    ),
                                    MovieRemoteDto(
                                        id = 4,
                                        title = "Movie 4",
                                        posterPath = null,
                                        voteAverage = null
                                    )
                                ),
                                totalPages = 1,
                                totalResults = 2
                            )
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                else -> {
                    error("Unhandled ${request.url.encodedPath}")
                }
            }
        }

        httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }

        remoteDataSource = RemoteMovieDiscoveryDataSourceImpl(httpClient)
    }



    @Test
    fun `getForYouMovies returns list of movies on success`() = runTest {
        val movies = remoteDataSource.getPersonalizedMovies()

        assertThat(movies).isNotEmpty()
        assertThat(movies.size).isEqualTo(2)
        assertThat(movies[0].id).isEqualTo(1)
        assertThat(movies[0].title).isEqualTo("Movie 1")
    }

    @Test
    fun `getForYouMovies returns empty list on empty results`() = runTest {
        mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/3/movie/top_rated" -> {
                    respond(
                        content = json.encodeToString(
                            SearchResultResponse<MovieRemoteDto>(
                                page = 1,
                                results = emptyList(),
                                totalPages = 0,
                                totalResults = 0
                            )
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                else -> error("Unhandled ${request.url.encodedPath}")
            }
        }
        httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) { json(json) }
        }
        remoteDataSource = RemoteMovieDiscoveryDataSourceImpl(httpClient)

        val movies = remoteDataSource.getPersonalizedMovies()
        assertThat(movies).isEmpty()
    }

    @Test
    fun `getForYouMovies filters out null movies or movies with null ID`() = runTest {
        mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/3/movie/top_rated" -> {
                    respond(
                        content = json.encodeToString(
                            SearchResultResponse(
                                page = 1,
                                results = listOf(
                                    MovieRemoteDto(
                                        id = 10,
                                        title = "Valid Movie",
                                        posterPath = null,
                                        voteAverage = null
                                    ),
                                    null,
                                    MovieRemoteDto(
                                        id = null,
                                        title = "Null ID Movie",
                                        posterPath = null,
                                        voteAverage = null
                                    )
                                ),
                                totalPages = 1,
                                totalResults = 3
                            )
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                else -> error("Unhandled ${request.url.encodedPath}")
            }
        }
        httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) { json(json) }
        }
        remoteDataSource = RemoteMovieDiscoveryDataSourceImpl(httpClient)

        val movies = remoteDataSource.getPersonalizedMovies()
        assertThat(movies).hasSize(1)
        assertThat(movies[0].id).isEqualTo(10)
    }

    @Test
    fun `getForYouMovies throws exception on API error`() = runTest {
        mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/3/movie/top_rated"-> {
                    respond(
                        content = "{}",
                        status = HttpStatusCode.InternalServerError,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                else -> error("Unhandled ${request.url.encodedPath}")
            }
        }
        httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) { json(json) }
        }
        remoteDataSource = RemoteMovieDiscoveryDataSourceImpl(httpClient)

        var thrownException: Throwable? = null
        try {
            remoteDataSource.getPersonalizedMovies()
        } catch (e: Exception) {
            thrownException = e
        }
        assertThat(thrownException).isNotNull()
    }


    @Test
    fun `getExploreMoreMovies returns list of movies on success`() = runTest {
        val movies = remoteDataSource.getSuggestedMovies()

        assertThat(movies).isNotEmpty()
        assertThat(movies.size).isEqualTo(2)
        assertThat(movies[0].id).isEqualTo(3)
        assertThat(movies[0].title).isEqualTo("Movie 3")
    }

    @Test
    fun `getExploreMoreMovies returns empty list on empty results`() = runTest {
        mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/3/movie/now_playing" -> {
                    respond(
                        content = json.encodeToString(
                            SearchResultResponse<MovieRemoteDto>(
                                page = 1,
                                results = emptyList(),
                                totalPages = 0,
                                totalResults = 0
                            )
                        ),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                else -> error("Unhandled ${request.url.encodedPath}")
            }
        }
        httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) { json(json) }
        }
        remoteDataSource = RemoteMovieDiscoveryDataSourceImpl(httpClient)

        val movies = remoteDataSource.getSuggestedMovies()
        assertThat(movies).isEmpty()
    }

}