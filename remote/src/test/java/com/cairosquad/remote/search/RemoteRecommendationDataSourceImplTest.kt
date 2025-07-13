package com.cairosquad.remote.search

import com.cairosquad.repository.search.data_source.remote.dto.MovieDto
import com.cairosquad.repository.search.data_source.remote.dto.SearchResultDto
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
    private lateinit var remoteDataSource: RemoteRecommendationDataSourceImpl

    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    @Before
    fun setup() {
        mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/3/movie/top_rated" -> {
                    respond(
                        content = json.encodeToString(
                            SearchResultDto(
                                page = 1,
                                results = listOf(
                                    MovieDto(
                                        id = 1,
                                        title = "Movie 1",
                                        posterPath = null,
                                        voteAverage = null
                                    ),
                                    MovieDto(
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
                            SearchResultDto(
                                page = 1,
                                results = listOf(
                                    MovieDto(
                                        id = 3,
                                        title = "Movie 3",
                                        posterPath = null,
                                        voteAverage = null
                                    ),
                                    MovieDto(
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

        remoteDataSource = RemoteRecommendationDataSourceImpl(httpClient)
    }


    @Test
    fun `should return list of Personalized movies when getPersonalizedMovies is successful`() = runTest {
        // Given

        // When
        val movies = remoteDataSource.getForYouMovies()

        // Then
        assertThat(movies).isNotEmpty()
        assertThat(movies.size).isEqualTo(2)
        assertThat(movies[0].id).isEqualTo(1)
        assertThat(movies[0].title).isEqualTo("Movie 1")
    }

    @Test
    fun `should return empty list when getPersonalizedMovies returns empty results`() = runTest {
        // Given
        mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/3/movie/top_rated" -> {
                    respond(
                        content = json.encodeToString(
                            SearchResultDto<MovieDto>(
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
        remoteDataSource = RemoteRecommendationDataSourceImpl(httpClient)

        // When
        val movies = remoteDataSource.getForYouMovies()

        // Then
        assertThat(movies).isEmpty()
    }

    @Test
    fun `should filter out null or invalid movies when getPersonalizedMovies is called`() = runTest {
        // Given
        mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/3/movie/top_rated" -> {
                    respond(
                        content = json.encodeToString(
                            SearchResultDto(
                                page = 1,
                                results = listOf(
                                    MovieDto(
                                        id = 10,
                                        title = "Valid Movie",
                                        posterPath = null,
                                        voteAverage = null
                                    ),
                                    null,
                                    MovieDto(
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
        remoteDataSource = RemoteRecommendationDataSourceImpl(httpClient)

        // When
        val movies = remoteDataSource.getForYouMovies()

        // Then
        assertThat(movies).hasSize(1)
        assertThat(movies[0].id).isEqualTo(10)
    }

    @Test
    fun `should throw exception when getPersonalizedMovies API returns error`() = runTest {
        // Given
        mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/3/movie/top_rated" -> {
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
        remoteDataSource = RemoteRecommendationDataSourceImpl(httpClient)

        // When
        var thrownException: Throwable? = null
        try {
            remoteDataSource.getForYouMovies()
        } catch (e: Exception) {
            thrownException = e
        }

        // Then
        assertThat(thrownException).isNotNull()
    }

    @Test
    fun `should return list of movies when getSuggestedMovies is successful`() = runTest {
        // Given

        // When
        val movies = remoteDataSource.getExploreMoreMovies()

        // Then
        assertThat(movies).isNotEmpty()
        assertThat(movies.size).isEqualTo(2)
        assertThat(movies[0].id).isEqualTo(3)
        assertThat(movies[0].title).isEqualTo("Movie 3")
    }

    @Test
    fun `should return empty list when getSuggestedMovies returns empty results`() = runTest {
        // Given
        mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/3/movie/now_playing" -> {
                    respond(
                        content = json.encodeToString(
                            SearchResultDto<MovieDto>(
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
        remoteDataSource = RemoteRecommendationDataSourceImpl(httpClient)

        // When
        val movies = remoteDataSource.getExploreMoreMovies()

        // Then
        assertThat(movies).isEmpty()
    }


}