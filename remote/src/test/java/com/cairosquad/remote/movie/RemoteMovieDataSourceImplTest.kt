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
    fun `getMovie should return MovieDetailsRemoteDto when response is valid`() = runBlocking {
        //given
        val client = createMockClient(MOVIE_RESPONSE)
        val dataSource = RemoteMovieDataSourceImpl(client)

        //when
        val result = dataSource.getMovie(10)

        //then
        assertEquals(10, result.id)
        assertEquals("Oppenheimer", result.title)
        assertEquals("2023-07-21", result.releaseDate)
    }

    @Test
    fun `getMovieReviews should return list of reviews when response has results`() = runBlocking {
        //given
        val client = createMockClient(REVIEW_RESPONSE)
        val dataSource = RemoteMovieDataSourceImpl(client)

        //when
        val result = dataSource.getMovieReviews(10, 1)

        //then
        assertEquals(2, result.size)
        assertEquals("Alice", result[0].author)
    }

    @Test
    fun `getSimilarMovies should return similar movies list when response is valid`() =
        runBlocking {
            //given
            val client = createMockClient(SIMILAR_MOVIES_RESPONSE)
            val dataSource = RemoteMovieDataSourceImpl(client)

            //when
            val result = dataSource.getSimilarMovies(10, 1)

            //then
            assertEquals(1, result.size)
            assertEquals("Tenet", result[0].title)
        }

    @Test
    fun `getMovieTopCast should return cast list when credits response is valid`() = runBlocking {
        //given
        val client = createMockClient(CREDITS_RESPONSE)
        val dataSource = RemoteMovieDataSourceImpl(client)

        //when
        val result = dataSource.getMovieTopCast(10, 1)

        //then
        assertEquals(2, result.size)
        assertEquals("Cillian Murphy", result[0].name)
        assertEquals("Emily Blunt", result[1].name)
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
        const val CREDITS_RESPONSE = """
            {
              "id": 10,
              "cast": [
                {
                  "id": 1,
                  "name": "Cillian Murphy",
                  "character": "J. Robert Oppenheimer"
                },
                {
                  "id": 2,
                  "name": "Emily Blunt",
                  "character": "Katherine Oppenheimer"
                }
              ]
            }
        """


    }
}
