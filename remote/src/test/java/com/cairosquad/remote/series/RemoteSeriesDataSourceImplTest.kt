package com.cairosquad.remote.series
import com.cairosquad.repository.movie.data_source.remote.dto.CreditResponse
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ResultResponse
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.*
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
class RemoteSeriesDataSourceImplTest {
    @Test
    fun `getSeries should return SeriesDetailsRemoteDto`() = runTest {
        val seriesId = 101L

        val mockEngine = MockEngine {
            respond(
                content = Json.encodeToString(
                    SeriesDetailsRemoteDto(
                        id = 101,
                        name = "Test Series"
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

        val dataSource = RemoteSeriesDataSourceImpl(httpClient)

        val result = dataSource.getSeries(seriesId)

        assertThat(result.id).isEqualTo(101)
        assertThat(result.name).isEqualTo("Test Series")
    }


    @Test
    fun `getSeriesReviews should return non-null reviews`() = runTest {
        val mockResponse = ResultResponse(
            results = listOf(
                ReviewRemoteDto(id = "abc", author = "user1", content = "Great!"),
                null
            )
        )

        val mockEngine = MockEngine {
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

        val dataSource = RemoteSeriesDataSourceImpl(httpClient)

        val result = dataSource.getSeriesReviews(seriesId = 2, page = 1)

        assertThat(result).hasSize(1)
        assertThat(result.first().id).isEqualTo("abc")
    }




}
