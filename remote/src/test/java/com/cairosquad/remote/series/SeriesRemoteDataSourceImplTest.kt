package com.cairosquad.remote.series

import com.cairosquad.remote.artists.SeriesListResponse
import com.cairosquad.repository.movie.data_source.remote.dto.GenreDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.EpisodeRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeasonRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeasonResponse
import com.cairosquad.repository.series.data_source.remote.dto.SeriesDetailsRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesResponse
import com.cairosquad.repository.utils.exception.ServerException
import com.cairosquad.repository.utils.sharedDto.remote.GenreResponse
import com.cairosquad.repository.utils.sharedDto.remote.ResultResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SeriesRemoteDataSourceImplTest {

    private lateinit var apiService: SeriesApiService
    private lateinit var remoteSeriesDataSource: SeriesRemoteDataSourceImpl

    @Before
    fun setup() {
        apiService = mockk()
        remoteSeriesDataSource = SeriesRemoteDataSourceImpl(apiService)
    }

    @Test
    fun `should return series details when getSeriesById is successful`() = runTest {
        val seriesId = 12345L
        coEvery { apiService.getSeriesById(seriesId) } returns seriesDetails

        val result = remoteSeriesDataSource.getSeriesById(seriesId)

        assertThat(result.id).isEqualTo(seriesId)
        assertThat(result.name).isEqualTo("Breaking Bad")
        assertThat(result.voteAverage).isEqualTo(9.3)
    }

    @Test
    fun `should throw ServerException when getSeriesById fails`() = runTest {
        val seriesId = 12345L
        coEvery { apiService.getSeriesById(seriesId) } throws ServerException("Internal Server Error")

        val exception = runCatching {
            remoteSeriesDataSource.getSeriesById(seriesId)
        }.exceptionOrNull()

        assertThat(exception).isInstanceOf(ServerException::class.java)
        assertThat(exception?.message).isEqualTo("Internal Server Error")
    }

    @Test
    fun `should return filtered reviews list when getSeriesReviews is successful`() = runTest {
        val seriesId = 456L
        coEvery { apiService.getSeriesReviews(seriesId, 1) } returns reviewResponse

        val result = remoteSeriesDataSource.getSeriesReviews(seriesId, 1)

        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo("rev1")
        assertThat(result[1].id).isEqualTo("rev2")
    }

    @Test
    fun `should throw ServerException when getSeriesReviews fails`() = runTest {
        val seriesId = 222L
        coEvery { apiService.getSeriesReviews(seriesId, 1) } throws ServerException("Internal Server Error")

        val exception = runCatching {
            remoteSeriesDataSource.getSeriesReviews(seriesId, 1)
        }.exceptionOrNull()

        assertThat(exception).isInstanceOf(ServerException::class.java)
        assertThat(exception?.message).isEqualTo("Internal Server Error")
    }

    @Test
    fun `should return empty list when reviews response results is null`() = runTest {
        val seriesId = 333L
        coEvery { apiService.getSeriesReviews(seriesId, 1) } returns emptyReviewResponse

        val result = remoteSeriesDataSource.getSeriesReviews(seriesId, 1)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return filtered similar series when getSimilarSeries is successful`() = runTest {
        val seriesId = 789L
        coEvery { apiService.getSimilarSeries(seriesId, 1) } returns similarSeriesResponse

        val result = remoteSeriesDataSource.getSimilarSeries(seriesId, 1)

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Better Call Saul")
        assertThat(result[1].name).isEqualTo("The Wire")
    }

    @Test
    fun `should return empty list when similar series response has only null results`() = runTest {
        val seriesId = 444L
        coEvery { apiService.getSimilarSeries(seriesId, 1) } returns emptySimilarSeriesResponse

        val result = remoteSeriesDataSource.getSimilarSeries(seriesId, 1)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return list with non-null IDs when getSeriesOfArtist is successful`() = runTest {
        val artistId = 7L
        coEvery { apiService.getSeriesOfArtist(artistId) } returns seriesOfArtistResponse

        val result = remoteSeriesDataSource.getSeriesOfArtist(artistId)

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(100)
    }

    @Test
    fun `should return filtered seasons list when getSeriesSeasons is successful`() = runTest {
        val seriesId = 654L
        coEvery { apiService.getSeriesSeasons(seriesId) } returns seasonsResponse

        val result = remoteSeriesDataSource.getSeriesSeasons(seriesId)

        assertThat(result).hasSize(2)
        assertThat(result[0].seasonNumber).isEqualTo(1)
        assertThat(result[1].seasonNumber).isEqualTo(2)
    }

    @Test
    fun `should return empty list when seasons response is null`() = runTest {
        val seriesId = 666L
        coEvery { apiService.getSeriesSeasons(seriesId) } returns emptySeasonsResponse

        val result = remoteSeriesDataSource.getSeriesSeasons(seriesId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return episodes list when getEpisodes is successful`() = runTest {
        val seriesId = 987L
        val seasonNumber = 1
        coEvery { apiService.getEpisodes(seriesId, seasonNumber) } returns episodesResponse

        val result = remoteSeriesDataSource.getEpisodes(seriesId, seasonNumber)

        assertThat(result).hasSize(2)
        assertThat(result[0].episodeNumber).isEqualTo(1)
        assertThat(result[1].episodeNumber).isEqualTo(2)
    }

    @Test
    fun `should return empty list when episodes response is null`() = runTest {
        val seriesId = 777L
        val seasonNumber = 1
        coEvery { apiService.getEpisodes(seriesId, seasonNumber) } returns emptyEpisodesResponse

        val result = remoteSeriesDataSource.getEpisodes(seriesId, seasonNumber)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return video key when getVideoKey is successful`() = runTest {
        val seriesId = 888L
        coEvery { apiService.getVideoKey(seriesId) } returns videoResponse

        val result = remoteSeriesDataSource.getVideoKey(seriesId)

        assertThat(result).isEqualTo("youtube_key_123")
    }

    @Test
    fun `should return empty string when getVideoKey returns null`() = runTest {
        val seriesId = 888L
        coEvery { apiService.getVideoKey(seriesId) } returns emptyVideoResponse

        val result = remoteSeriesDataSource.getVideoKey(seriesId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return top rated series when getTopRatingSeries is successful`() = runTest {
        val page = 1
        val genreId = 18L
        coEvery { apiService.getTopRatingSeries(page, genreId) } returns topRatedSeriesResponse

        val result = remoteSeriesDataSource.getTopRatingSeries(page, genreId)

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Game of Thrones")
        assertThat(result[1].name).isEqualTo("Stranger Things")
    }

    @Test
    fun `should return empty list when top rated series response is null`() = runTest {
        val page = 1
        val genreId = 18L
        coEvery { apiService.getTopRatingSeries(page, genreId) } returns emptySeriesResponse

        val result = remoteSeriesDataSource.getTopRatingSeries(page, genreId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return recommended series when getMoreRecommendedSeries is successful`() = runTest {
        val page = 1
        val genreId = 35L
        coEvery { apiService.getMoreRecommendedSeries(page, genreId) } returns recommendedSeriesResponse

        val result = remoteSeriesDataSource.getMoreRecommendedSeries(page, genreId)

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Friends")
        assertThat(result[1].name).isEqualTo("The Office")
    }

    @Test
    fun `should return empty list when recommended series response is null`() = runTest {
        val page = 1
        val genreId = 35L
        coEvery { apiService.getMoreRecommendedSeries(page, genreId) } returns emptySeriesResponse

        val result = remoteSeriesDataSource.getMoreRecommendedSeries(page, genreId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return on tv series when getOnTvSeries is successful`() = runTest {
        val page = 1
        val genreId = 10765L
        coEvery { apiService.getOnTvSeries(page, genreId, any(), any()) } returns onTvSeriesResponse

        val result = remoteSeriesDataSource.getOnTvSeries(page, genreId)

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("The Mandalorian")
        assertThat(result[1].name).isEqualTo("WandaVision")
    }

    @Test
    fun `should return empty list when on tv series response is null`() = runTest {
        val page = 1
        val genreId = 10765L
        coEvery { apiService.getOnTvSeries(page, genreId, any(), any()) } returns emptySeriesResponse

        val result = remoteSeriesDataSource.getOnTvSeries(page, genreId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return airing today series when getAiringTodaySeries is successful`() = runTest {
        val page = 1
        val genreId = 80L
        coEvery { apiService.getAiringTodaySeries(page, genreId, any(), any()) } returns airingTodaySeriesResponse

        val result = remoteSeriesDataSource.getAiringTodaySeries(page, genreId)

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("NCIS")
        assertThat(result[1].name).isEqualTo("FBI")
    }

    @Test
    fun `should return empty list when airing today series response is null`() = runTest {
        val page = 1
        val genreId = 80L
        coEvery { apiService.getAiringTodaySeries(page, genreId, any(), any()) } returns emptySeriesResponse

        val result = remoteSeriesDataSource.getAiringTodaySeries(page, genreId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return trending series when getTrendingSeries is successful`() = runTest {
        val page = 1
        val genreId = 18L
        coEvery { apiService.getTrendingSeries(page, genreId, any(), any()) } returns trendingSeriesResponse

        val result = remoteSeriesDataSource.getTrendingSeries(page, genreId)

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Succession")
        assertThat(result[1].name).isEqualTo("Yellowjackets")
    }

    @Test
    fun `should return empty list when trending series response is null`() = runTest {
        val page = 1
        val genreId = 18L
        coEvery { apiService.getTrendingSeries(page, genreId, any(), any()) } returns emptySeriesResponse

        val result = remoteSeriesDataSource.getTrendingSeries(page, genreId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return free to watch series when getFreeToWatchSeries is successful`() = runTest {
        val page = 1
        val genreId = 35L
        coEvery { apiService.getFreeToWatchSeries(page, genreId) } returns freeToWatchSeriesResponse

        val result = remoteSeriesDataSource.getFreeToWatchSeries(page, genreId)

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Brooklyn Nine-Nine")
        assertThat(result[1].name).isEqualTo("Parks and Recreation")
    }

    @Test
    fun `should return empty list when free to watch series response is null`() = runTest {
        val page = 1
        val genreId = 35L
        coEvery { apiService.getFreeToWatchSeries(page, genreId) } returns emptySeriesResponse

        val result = remoteSeriesDataSource.getFreeToWatchSeries(page, genreId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return series by category when getSeriesByCategory is successful`() = runTest {
        val genreId = 10759L
        val page = 1
        coEvery { apiService.getSeriesByCategory(genreId, page) } returns seriesByCategoryResponse

        val result = remoteSeriesDataSource.getSeriesByCategory(genreId, page)

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("The Witcher")
        assertThat(result[1].name).isEqualTo("Vikings")
    }

    @Test
    fun `should return empty list when series by category response is null`() = runTest {
        val genreId = 10759L
        val page = 1
        coEvery { apiService.getSeriesByCategory(genreId, page) } returns emptySeriesResponse

        val result = remoteSeriesDataSource.getSeriesByCategory(genreId, page)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return genres list when getSeriesGenres is successful`() = runTest {
        coEvery { apiService.getSeriesGenres() } returns genreResponse

        val result = remoteSeriesDataSource.getSeriesGenres()

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Drama")
        assertThat(result[1].name).isEqualTo("Comedy")
    }

    @Test
    fun `should return empty list when genres response is null`() = runTest {
        coEvery { apiService.getSeriesGenres() } returns emptyGenreResponse

        val result = remoteSeriesDataSource.getSeriesGenres()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return popular series when getPopularSeries is successful`() = runTest {
        val page = 1
        val genreId = 10751L
        coEvery { apiService.getPopularSeries(page, genreId) } returns popularSeriesResponse

        val result = remoteSeriesDataSource.getPopularSeries(page, genreId)

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Modern Family")
        assertThat(result[1].name).isEqualTo("The Simpsons")
    }

    @Test
    fun `should return empty list when popular series response is null`() = runTest {
        val page = 1
        val genreId = 10751L
        coEvery { apiService.getPopularSeries(page, genreId) } returns emptySeriesResponse

        val result = remoteSeriesDataSource.getPopularSeries(page, genreId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return all series when getAllSeries is successful`() = runTest {
        val page = 1
        val genreId = 9648L
        val sortBy = "popularity.desc"
        coEvery { apiService.getAllSeries(page, genreId, sortBy) } returns allSeriesResponse

        val result = remoteSeriesDataSource.getAllSeries(page, genreId, sortBy)

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Sherlock")
        assertThat(result[1].name).isEqualTo("True Detective")
    }

    @Test
    fun `should return empty list when all series response is null`() = runTest {
        val page = 1
        val genreId = 9648L
        val sortBy = "popularity.desc"
        coEvery { apiService.getAllSeries(page, genreId, sortBy) } returns emptySeriesResponse

        val result = remoteSeriesDataSource.getAllSeries(page, genreId, sortBy)

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return series by query when getSeriesByQuery is successful`() = runTest {
        val query = "Breaking"
        val page = 1
        coEvery { apiService.getSeriesByQuery(query, page) } returns seriesByQueryResponse

        val result = remoteSeriesDataSource.getSeriesByQuery(query, page)

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Breaking Bad")
        assertThat(result[1].name).isEqualTo("Better Call Saul")
    }

    @Test
    fun `should return empty list when series by query response is null`() = runTest {
        val query = "Breaking"
        val page = 1
        coEvery { apiService.getSeriesByQuery(query, page) } returns emptySeriesResponse

        val result = remoteSeriesDataSource.getSeriesByQuery(query, page)

        assertThat(result).isEmpty()
    }

    private companion object {
        val seriesDetails = SeriesDetailsRemoteDto(
            id = 12345L,
            name = "Breaking Bad",
            overview = "A high school chemistry teacher turned meth cook",
            posterPath = "/breaking_bad.jpg",
            backdropPath = "/bb_backdrop.jpg",
            firstAirDate = "2008-01-20",
            voteAverage = 9.3,
            numberOfSeasons = 5,
            numberOfEpisodes = 62
        )

        val reviewResponse = ResultResponse(
            page = 1,
            results = listOf(
                ReviewRemoteDto(id = "rev1", author = "John Doe", content = "Amazing series!"),
                null,
                ReviewRemoteDto(id = "rev2", author = "Jane Smith", content = "Great acting!")
            ),
            totalPages = 1,
            totalResults = 3
        )

        val emptyReviewResponse = ResultResponse<ReviewRemoteDto>(
            page = 1,
            results = null,
            totalPages = 0,
            totalResults = 0
        )

        val similarSeriesResponse = ResultResponse(
            page = 1,
            results = listOf(
                SeriesRemoteDto(id = 101, name = "Better Call Saul", posterPath = "/bcs.jpg"),
                null,
                SeriesRemoteDto(id = 102, name = "The Wire", posterPath = "/wire.jpg")
            ),
            totalPages = 1,
            totalResults = 3
        )

        val emptySimilarSeriesResponse = ResultResponse<SeriesRemoteDto>(
            page = 1,
            results = listOf(null, null),
            totalPages = 1,
            totalResults = 2
        )

        val seriesOfArtistResponse = SeriesListResponse(
            series = listOf(SeriesRemoteDto(id = 100, name = "Series A", posterPath = "/a.jpg"))
        )

        val seasonsResponse = SeriesResponse(
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

        val emptySeasonsResponse = SeriesResponse(
            name = "Test Series",
            numberOfEpisodes = 0,
            numberOfSeasons = 0,
            seasons = null
        )

        val episodesResponse = SeasonResponse(
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
                    seriesId = 987L
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
                    seriesId = 987L
                )
            )
        )

        val emptyEpisodesResponse = SeasonResponse(id = 301, episodes = null)

        val videoResponse = mockk<com.cairosquad.repository.movie.data_source.remote.dto.VideoResponse> {
            coEvery { getVideoKey() } returns "youtube_key_123"
        }

        val emptyVideoResponse = mockk<com.cairosquad.repository.movie.data_source.remote.dto.VideoResponse> {
            coEvery { getVideoKey() } returns null
        }

        val topRatedSeriesResponse = ResultResponse(
            page = 1,
            results = listOf(
                SeriesRemoteDto(id = 103, name = "Game of Thrones", posterPath = "/got.jpg"),
                null,
                SeriesRemoteDto(id = 104, name = "Stranger Things", posterPath = "/st.jpg")
            ),
            totalPages = 1,
            totalResults = 3
        )

        val recommendedSeriesResponse = ResultResponse(
            page = 1,
            results = listOf(
                SeriesRemoteDto(id = 105, name = "Friends", posterPath = "/friends.jpg"),
                null,
                SeriesRemoteDto(id = 106, name = "The Office", posterPath = "/office.jpg")
            ),
            totalPages = 1,
            totalResults = 3
        )

        val onTvSeriesResponse = ResultResponse(
            page = 1,
            results = listOf(
                SeriesRemoteDto(id = 107, name = "The Mandalorian", posterPath = "/mandalorian.jpg"),
                null,
                SeriesRemoteDto(id = 108, name = "WandaVision", posterPath = "/wandavision.jpg")
            ),
            totalPages = 1,
            totalResults = 3
        )

        val airingTodaySeriesResponse = ResultResponse(
            page = 1,
            results = listOf(
                SeriesRemoteDto(id = 109, name = "NCIS", posterPath = "/ncis.jpg"),
                null,
                SeriesRemoteDto(id = 110, name = "FBI", posterPath = "/fbi.jpg")
            ),
            totalPages = 1,
            totalResults = 3
        )

        val trendingSeriesResponse = ResultResponse(
            page = 1,
            results = listOf(
                SeriesRemoteDto(id = 111, name = "Succession", posterPath = "/succession.jpg"),
                null,
                SeriesRemoteDto(id = 112, name = "Yellowjackets", posterPath = "/yellowjackets.jpg")
            ),
            totalPages = 1,
            totalResults = 3
        )

        val freeToWatchSeriesResponse = ResultResponse(
            page = 1,
            results = listOf(
                SeriesRemoteDto(id = 113, name = "Brooklyn Nine-Nine", posterPath = "/b99.jpg"),
                null,
                SeriesRemoteDto(id = 114, name = "Parks and Recreation", posterPath = "/parks.jpg")
            ),
            totalPages = 1,
            totalResults = 3
        )

        val seriesByCategoryResponse = ResultResponse(
            page = 1,
            results = listOf(
                SeriesRemoteDto(id = 115, name = "The Witcher", posterPath = "/witcher.jpg"),
                null,
                SeriesRemoteDto(id = 116, name = "Vikings", posterPath = "/vikings.jpg")
            ),
            totalPages = 1,
            totalResults = 3
        )

        val genreResponse = GenreResponse(
            genres = listOf(
                GenreDto(id = 18, name = "Drama"),
                null,
                GenreDto(id = 35, name = "Comedy")
            )
        )

        val emptyGenreResponse = GenreResponse(genres = null)

        val popularSeriesResponse = ResultResponse(
            page = 1,
            results = listOf(
                SeriesRemoteDto(id = 117, name = "Modern Family", posterPath = "/modernfamily.jpg"),
                null,
                SeriesRemoteDto(id = 118, name = "The Simpsons", posterPath = "/simpsons.jpg")
            ),
            totalPages = 1,
            totalResults = 3
        )

        val allSeriesResponse = ResultResponse(
            page = 1,
            results = listOf(
                SeriesRemoteDto(id = 119, name = "Sherlock", posterPath = "/sherlock.jpg"),
                null,
                SeriesRemoteDto(id = 120, name = "True Detective", posterPath = "/truedetective.jpg")
            ),
            totalPages = 1,
            totalResults = 3
        )

        val seriesByQueryResponse = ResultResponse(
            page = 1,
            results = listOf(
                SeriesRemoteDto(id = 12345, name = "Breaking Bad", posterPath = "/breaking_bad.jpg"),
                null,
                SeriesRemoteDto(id = 101, name = "Better Call Saul", posterPath = "/bcs.jpg")
            ),
            totalPages = 1,
            totalResults = 3
        )

        val emptySeriesResponse = ResultResponse<SeriesRemoteDto>(
            page = 1,
            results = null,
            totalPages = 0,
            totalResults = 0
        )
    }
}