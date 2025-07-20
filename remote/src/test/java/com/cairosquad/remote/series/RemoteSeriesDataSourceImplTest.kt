package com.cairosquad.remote.series

import com.cairosquad.repository.movie.data_source.remote.dto.CreditResponse
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ResultResponse
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.EpisodeRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeasonRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeasonResponse
import com.cairosquad.repository.series.data_source.remote.dto.SeriesDetailsRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesResponse
import com.cairosquad.repository.utils.exception.EmptyResponseException
import com.cairosquad.repository.utils.exception.ServerException
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class RemoteSeriesDataSourceImplTest {

    private lateinit var apiService: SeriesApiService
    private lateinit var remoteSeriesDataSource: RemoteSeriesDataSourceImpl

    @Before
    fun setup() {
        apiService = mockk()
        remoteSeriesDataSource = spyk(RemoteSeriesDataSourceImpl(apiService))
    }

    @Test
    fun `should return series details when getSeries is successful`() = runTest {
        // Given
        val seriesId = 12345L
        val seriesDetails = SeriesDetailsRemoteDto(
            id = seriesId,
            name = "Breaking Bad",
            overview = "A high school chemistry teacher turned meth cook",
            posterPath = "/breaking_bad.jpg",
            backdropPath = "/bb_backdrop.jpg",
            firstAirDate = "2008-01-20",
            voteAverage = 9.3,
            numberOfSeasons = 5,
            numberOfEpisodes = 62
        )
        coEvery { apiService.getSeries(seriesId) } returns Response.success(seriesDetails)

        // When
        val result = remoteSeriesDataSource.getSeries(seriesId)

        // Then
        assertThat(result.id).isEqualTo(seriesId)
        assertThat(result.name).isEqualTo("Breaking Bad")
        assertThat(result.voteAverage).isEqualTo(9.3)
    }

    @Test
    fun `should return filtered reviews list when getSeriesReviews is successful`() = runTest {
        // Given
        val seriesId = 456L
        val response = ResultResponse(
            page = 1,
            results = listOf(
                ReviewRemoteDto(id = "rev1", author = "John Doe", content = "Amazing series!"),
                null,
                ReviewRemoteDto(id = "rev2", author = "Jane Smith", content = "Great acting!")
            ),
            totalPages = 1,
            totalResults = 3
        )
        coEvery { apiService.getSeriesReviews(seriesId, 1) } returns Response.success(response)

        // When
        val result = remoteSeriesDataSource.getSeriesReviews(seriesId, 1)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo("rev1")
        assertThat(result[1].id).isEqualTo("rev2")
        assertThat(result[0].author).isEqualTo("John Doe")
    }

    @Test
    fun `should return filtered similar series when getSimilarSeries is successful`() = runTest {
        // Given
        val seriesId = 789L
        val response = ResultResponse(
            page = 1,
            results = listOf(
                SeriesRemoteDto(id = 101, name = "Better Call Saul", posterPath = "/bcs.jpg"),
                null,
                SeriesRemoteDto(id = 102, name = "The Wire", posterPath = "/wire.jpg")
            ),
            totalPages = 1,
            totalResults = 3
        )
        coEvery { apiService.getSimilarSeries(seriesId, 1) } returns Response.success(response)

        // When
        val result = remoteSeriesDataSource.getSimilarSeries(seriesId, 1)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Better Call Saul")
        assertThat(result[1].name).isEqualTo("The Wire")
    }

    @Test
    fun `should return filtered cast list when getSeriesTopCast is successful`() = runTest {
        // Given
        val seriesId = 321L
        val response = CreditResponse(
            id = 12345,
            cast = listOf(
                ArtistRemoteDto(
                    id = 201,
                    name = "Bryan Cranston",
                    profilePath = "/bryan.jpg",
                    placeOfBirth = "Hollywood, California",
                    birthday = "1956-03-07",
                    biography = "American actor and producer",
                    department = "Acting"
                ),
                null,
                ArtistRemoteDto(
                    id = 202,
                    name = "Aaron Paul",
                    profilePath = "/aaron.jpg",
                    placeOfBirth = "Emmett, Idaho",
                    birthday = "1979-08-27",
                    biography = "American actor",
                    department = "Acting"
                )
            )
        )
        coEvery { apiService.getSeriesTopCast(seriesId, 1) } returns Response.success(response)

        // When
        val result = remoteSeriesDataSource.getSeriesTopCast(seriesId, 1)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Bryan Cranston")
        assertThat(result[1].name).isEqualTo("Aaron Paul")
        assertThat(result[0].id).isEqualTo(201)
        assertThat(result[1].id).isEqualTo(202)
    }

    @Test
    fun `should return filtered seasons list when getSeriesSeasons is successful`() = runTest {
        // Given
        val seriesId = 654L
        val response = SeriesResponse(
            name = "Breaking Bad",
            numberOfEpisodes = 62,
            numberOfSeasons = 5,
            seasons = listOf(
                SeasonRemoteDto(
                    id = 301,
                    seasonNumber = 1,
                    name = "Season 1",
                    episodeCount = 7,
                    airDate = "2008-01-20",
                    overview = "First season",
                    posterPath = "/season1.jpg",
                    voteAverage = 8.5
                ),
                null,
                SeasonRemoteDto(
                    id = 302,
                    seasonNumber = 2,
                    name = "Season 2",
                    episodeCount = 13,
                    airDate = "2009-03-08",
                    overview = "Second season",
                    posterPath = "/season2.jpg",
                    voteAverage = 9.0
                )
            )
        )
        coEvery { apiService.getSeriesSeasons(seriesId) } returns Response.success(response)

        // When
        val result = remoteSeriesDataSource.getSeriesSeasons(seriesId)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].seasonNumber).isEqualTo(1)
        assertThat(result[1].seasonNumber).isEqualTo(2)
        assertThat(result[0].name).isEqualTo("Season 1")
        assertThat(result[0].episodeCount).isEqualTo(7)
        assertThat(result[1].episodeCount).isEqualTo(13)
    }

    @Test
    fun `should return episodes list when getEpisodes is successful`() = runTest {
        // Given
        val seriesId = 987L
        val seasonNumber = 1
        val response = SeasonResponse(
            id = 301,
            episodes = listOf(
                EpisodeRemoteDto(
                    id = 401,
                    episodeNumber = 1,
                    name = "Pilot",
                    overview = "First episode",
                    airDate = "2008-01-20",
                    runtime = 45,
                    seasonNumber = 1,
                    stillPath = "/pilot.jpg",
                    voteAverage = 8.2,
                    seriesId = seriesId
                ),
                EpisodeRemoteDto(
                    id = 402,
                    episodeNumber = 2,
                    name = "Cat's in the Bag",
                    overview = "Second episode",
                    airDate = "2008-01-27",
                    runtime = 48,
                    seasonNumber = 1,
                    stillPath = "/episode2.jpg",
                    voteAverage = 8.5,
                    seriesId = seriesId
                )
            )
        )
        coEvery { apiService.getEpisodes(seriesId, seasonNumber) } returns Response.success(response)

        // When
        val result = remoteSeriesDataSource.getEpisodes(seriesId, seasonNumber)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].episodeNumber).isEqualTo(1)
        assertThat(result[1].episodeNumber).isEqualTo(2)
        assertThat(result[0].name).isEqualTo("Pilot")
        assertThat(result[0].runtime).isEqualTo(45)
        assertThat(result[1].runtime).isEqualTo(48)
        assertThat(result[0].seriesId).isEqualTo(seriesId)
    }

    @Test
    fun `should throw EmptyResponseException when series response body is null`() = runTest {
        // Given
        val seriesId = 111L
        coEvery { apiService.getSeries(seriesId) } returns Response.success(null)

        // When & Then
        var exception: Throwable? = null
        try {
            remoteSeriesDataSource.getSeries(seriesId)
        } catch (e: Throwable) {
            exception = e
        }

        assertThat(exception).isInstanceOf(EmptyResponseException::class.java)
    }

    @Test
    fun `should throw ServerException when API returns 500 for reviews`() = runTest {
        // Given
        val seriesId = 222L
        coEvery { apiService.getSeriesReviews(seriesId, 1) } returns Response.error(
            500,
            """{ "status_message": "Internal Server Error" }""".toResponseBody(null)
        )

        // When & Then
        var exception: Throwable? = null
        try {
            remoteSeriesDataSource.getSeriesReviews(seriesId, 1)
        } catch (e: Throwable) {
            exception = e
        }

        assertThat(exception).isInstanceOf(ServerException::class.java)
        assertThat(exception?.message).isEqualTo("Internal Server Error")
    }

    @Test
    fun `should return empty list when reviews response results is null`() = runTest {
        // Given
        val seriesId = 333L
        val response = ResultResponse<ReviewRemoteDto>(
            page = 1,
            results = null,
            totalPages = 0,
            totalResults = 0
        )
        coEvery { apiService.getSeriesReviews(seriesId, 1) } returns Response.success(response)

        // When
        val result = remoteSeriesDataSource.getSeriesReviews(seriesId, 1)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when similar series response has only null results`() = runTest {
        // Given
        val seriesId = 444L
        val response = ResultResponse(
            page = 1,
            results = listOf<SeriesRemoteDto?>(null, null),
            totalPages = 1,
            totalResults = 2
        )
        coEvery { apiService.getSimilarSeries(seriesId, 1) } returns Response.success(response)

        // When
        val result = remoteSeriesDataSource.getSimilarSeries(seriesId, 1)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when cast response is null`() = runTest {
        // Given
        val seriesId = 555L
        val response = CreditResponse(id = 12345, cast = null)
        coEvery { apiService.getSeriesTopCast(seriesId, 1) } returns Response.success(response)

        // When
        val result = remoteSeriesDataSource.getSeriesTopCast(seriesId, 1)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when seasons response is null`() = runTest {
        // Given
        val seriesId = 666L
        val response = SeriesResponse(
            name = "Test Series",
            numberOfEpisodes = 0,
            numberOfSeasons = 0,
            seasons = null
        )
        coEvery { apiService.getSeriesSeasons(seriesId) } returns Response.success(response)

        // When
        val result = remoteSeriesDataSource.getSeriesSeasons(seriesId)

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty list when episodes response is null`() = runTest {
        // Given
        val seriesId = 777L
        val seasonNumber = 1
        val response = SeasonResponse(id = 301, episodes = null)
        coEvery { apiService.getEpisodes(seriesId, seasonNumber) } returns Response.success(response)

        // When
        val result = remoteSeriesDataSource.getEpisodes(seriesId, seasonNumber)

        // Then
        assertThat(result).isEmpty()
    }

}