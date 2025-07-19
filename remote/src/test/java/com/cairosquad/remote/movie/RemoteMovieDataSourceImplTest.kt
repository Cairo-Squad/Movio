package com.cairosquad.remote.movie

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class RemoteMovieDataSourceImplTest {


    private fun createMockClient(
        responseJson: String,
        status: HttpStatusCode = HttpStatusCode.OK
    ): HttpClient {
        return HttpClient(MockEngine) {
            engine {
                addHandler {
                    respond(
                        content = responseJson,
                        status = status,
                        headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                    )
                }
            }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    @Test
    fun `getMovie - Given valid response - When getMovie called - Then return MovieDetailsRemoteDto`() =
        runBlocking {
            val client = createMockClient(MOVIE_RESPONSE)
            val dataSource = RemoteMovieDataSourceImpl(client)
            val result = dataSource.getMovie(10)
            assertEquals(10, result.id)
            assertEquals("Oppenheimer", result.title)
            assertEquals("2023-07-21", result.releaseDate)
        }

    @Test
    fun `getMovieReviews - Given list of reviews - When called - Then return non-empty list`() =
        runBlocking {
            val client = createMockClient(REVIEW_RESPONSE)
            val dataSource = RemoteMovieDataSourceImpl(client)
            val result = dataSource.getMovieReviews(10, 1)
            assertEquals(2, result.size)
            assertEquals("Alice", result[0].author)
        }

    @Test
    fun `getSimilarMovies - Given list of movies - When called - Then return MovieRemoteDto list`() =
        runBlocking {
            val client = createMockClient(SIMILAR_MOVIES_RESPONSE)
            val dataSource = RemoteMovieDataSourceImpl(client)
            val result = dataSource.getSimilarMovies(10, 1)
            assertEquals(1, result.size)
            assertEquals("Tenet", result[0].title)
        }

    private companion object {
        const val MOVIE_RESPONSE = """
            {
              "id": 10,
              "title": "Oppenheimer",
              "overview": "A brilliant physicist...",
              "release_date": "2023-07-21",
              "vote_average": 9.2
            }
        """

        const val REVIEW_RESPONSE = """
            {
              "results": [
                {
                  "id": "r1",
                  "author": "Alice",
                  "content": "Great movie!"
                },
                {
                  "id": "r2",
                  "author": "Bob",
                  "content": "Masterpiece!"
                }
              ]
            }
        """

        const val SIMILAR_MOVIES_RESPONSE = """
            {
              "results": [
                {
                  "id": 21,
                  "title": "Tenet",
                  "overview": "Time inversion"
                }
              ]
            }
        """


    }
}
