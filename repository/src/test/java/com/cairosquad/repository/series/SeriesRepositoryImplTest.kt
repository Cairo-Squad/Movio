package com.cairosquad.repository.series

import com.cairosquad.domain.exception.DomainEmptyResponseException
import com.cairosquad.domain.model.SortType
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series
import com.cairosquad.repository.movie.data_source.remote.dto.AuthorDetailsDto
import com.cairosquad.repository.movie.data_source.remote.dto.GenreDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.series.data_source.local.SeasonEpisodeLocalDataSource
import com.cairosquad.repository.series.data_source.local.SeriesLocalDataSource
import com.cairosquad.repository.series.data_source.local.dto.EpisodeCacheDto
import com.cairosquad.repository.series.data_source.local.dto.GenreOfSeriesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.SeasonCacheDto
import com.cairosquad.repository.series.data_source.local.dto.SeriesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.SeriesWithoutGenreCacheDto
import com.cairosquad.repository.series.data_source.remote.SeriesRemoteDataSource
import com.cairosquad.repository.series.data_source.remote.dto.EpisodeRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeasonRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesDetailsRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.utils.exception.RepoEmptyResponseException
import com.cairosquad.repository.utils.sharedDto.local.ReviewCacheDto
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfAiringTodaySeries
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfAllSeries
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfEpisodes
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfFreeToWatchSeries
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfMoreRecommendedSeries
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfOnTvSeries
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfPopularSeries
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSearchedSeries
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSeries
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSeriesByCategory
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSeriesOfArtist
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSeriesReviews
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSeriesSeasons
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSimilarSeries
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfTopRatedSeries
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfTrendingSeries
import com.google.common.truth.Truth.assertThat
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class SeriesRepositoryImplTest {

    private lateinit var repository: SeriesRepositoryImpl
    private lateinit var remoteDataSource: SeriesRemoteDataSource
    private lateinit var localDataSource: SeriesLocalDataSource
    private lateinit var seasonEpisodeLocalDataSource: SeasonEpisodeLocalDataSource

    @Before
    fun setUp() {
        remoteDataSource = mockk(relaxed = true)
        localDataSource = mockk(relaxed = true)
        seasonEpisodeLocalDataSource = mockk(relaxed = true)
        repository =
            SeriesRepositoryImpl(remoteDataSource, localDataSource, seasonEpisodeLocalDataSource)
    }

    @Test
    fun `should return cached series when getSeriesById is called and cache is available`() =
        runTest {
            val seriesId = 1L
            val cacheCode = getCacheCodeOfSeries(seriesId)
            val cachedSeries = listOf(cachedSeriesDto)
            coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
            coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns cachedSeries

            val result = repository.getSeriesById(seriesId)

            assertThat(result).isEqualTo(expectedSeries)
            coVerify(exactly = 1) { localDataSource.getSeriesByCacheCode(cacheCode) }
            coVerify(exactly = 0) { remoteDataSource.getSeriesById(any()) }
        }

    @Test
    fun `should fetch data from remote when getSeriesById is called and cache is empty`() =
        runTest {
            val seriesId = 1L
            val cacheCode = getCacheCodeOfSeries(seriesId)
            coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
            coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns emptyList()
            coEvery { remoteDataSource.getSeriesById(seriesId) } returns seriesDetailsRemoteDto
            coEvery { remoteDataSource.getVideoKey(seriesId) } returns "trailer_key"
            coEvery { localDataSource.insertCacheCodeWithSeries(any()) } just Runs

            val result = repository.getSeriesById(seriesId)

            assertThat(result).isEqualTo(expectedSeries)
            coVerify(exactly = 1) { remoteDataSource.getSeriesById(seriesId) }
            coVerify(exactly = 1) { localDataSource.insertCacheCodeWithSeries(any()) }
        }

    @Test
    fun `should throw DomainEmptyResponseException when getSeriesById is called and remote returns empty`() =
        runTest {
            val seriesId = 1L
            val cacheCode = getCacheCodeOfSeries(seriesId)
            coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
            coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns emptyList()
            coEvery { remoteDataSource.getSeriesById(seriesId) } throws RepoEmptyResponseException()

            assertFailsWith<DomainEmptyResponseException> {
                repository.getSeriesById(seriesId)
            }
            coVerify(exactly = 1) { remoteDataSource.getSeriesById(seriesId) }
            coVerify(exactly = 0) { localDataSource.insertCacheCodeWithSeries(any()) }
        }

    @Test
    fun `should return cached reviews when getSeriesReviews is called and cache is available`() =
        runTest {
            val seriesId = 1L
            val page = 1
            val cacheCode = getCacheCodeOfSeriesReviews(page, seriesId)
            val cachedReviews = listOf(cachedReviewDto)
            coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
            coEvery { localDataSource.getSeriesReviewsByCacheCode(cacheCode) } returns cachedReviews

            val result = repository.getSeriesReviews(seriesId, page)

            assertThat(result).isEqualTo(listOf(expectedReview))
            coVerify(exactly = 1) { localDataSource.getSeriesReviewsByCacheCode(cacheCode) }
            coVerify(exactly = 0) { remoteDataSource.getSeriesReviews(any(), any()) }
        }

    @Test
    fun `should fetch data from remote when getSeriesReviews is called and cache is empty`() =
        runTest {
            val seriesId = 1L
            val page = 1
            val cacheCode = getCacheCodeOfSeriesReviews(page, seriesId)
            coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
            coEvery { localDataSource.getSeriesReviewsByCacheCode(cacheCode) } returns emptyList()
            coEvery { remoteDataSource.getSeriesReviews(seriesId, page) } returns listOf(
                reviewRemoteDto
            )
            coEvery { localDataSource.insertCacheCodeWithReviews(any()) } just Runs

            val result = repository.getSeriesReviews(seriesId, page)

            assertThat(result).isEqualTo(listOf(expectedReview))
            coVerify(exactly = 1) { remoteDataSource.getSeriesReviews(seriesId, page) }
            coVerify(exactly = 1) { localDataSource.insertCacheCodeWithReviews(any()) }
        }

    @Test
    fun `should return cached seasons when getSeriesSeasons is called and cache is available`() =
        runTest {
            val seriesId = 1L
            val cacheCode = getCacheCodeOfSeriesSeasons(seriesId)
            val cachedSeasons = listOf(cachedSeasonDto)
            coEvery { seasonEpisodeLocalDataSource.deleteExpiredCache(any()) } just Runs
            coEvery { seasonEpisodeLocalDataSource.getSeasonsByCacheCode(cacheCode) } returns cachedSeasons

            val result = repository.getSeriesSeasons(seriesId)

            assertThat(result).isEqualTo(listOf(expectedSeason))
            coVerify(exactly = 1) { seasonEpisodeLocalDataSource.getSeasonsByCacheCode(cacheCode) }
            coVerify(exactly = 0) { remoteDataSource.getSeriesSeasons(any()) }
        }

    @Test
    fun `should fetch data from remote when getSeriesSeasons is called and cache is empty`() =
        runTest {
            val seriesId = 1L
            val cacheCode = getCacheCodeOfSeriesSeasons(seriesId)
            coEvery { seasonEpisodeLocalDataSource.deleteExpiredCache(any()) } just Runs
            coEvery { seasonEpisodeLocalDataSource.getSeasonsByCacheCode(cacheCode) } returns emptyList()
            coEvery { remoteDataSource.getSeriesSeasons(seriesId) } returns listOf(seasonRemoteDto)
            coEvery { seasonEpisodeLocalDataSource.insertCacheCodeWithSeasons(any()) } just Runs

            val result = repository.getSeriesSeasons(seriesId)

            assertThat(result).isEqualTo(listOf(expectedSeason))
            coVerify(exactly = 1) { remoteDataSource.getSeriesSeasons(seriesId) }
            coVerify(exactly = 1) { seasonEpisodeLocalDataSource.insertCacheCodeWithSeasons(any()) }
        }

    @Test
    fun `should return cached episodes when getEpisodes is called and cache is available`() =
        runTest {
            val seriesId = 1L
            val seasonNumber = 1
            val cacheCode = getCacheCodeOfEpisodes(seriesId, seasonNumber)
            val cachedEpisodes = listOf(cachedEpisodeDto)
            coEvery { seasonEpisodeLocalDataSource.deleteExpiredCache(any()) } just Runs
            coEvery { seasonEpisodeLocalDataSource.getEpisodesByCacheCode(cacheCode) } returns cachedEpisodes

            val result = repository.getEpisodes(seriesId, seasonNumber)

            assertThat(result).isEqualTo(listOf(expectedEpisode))
            coVerify(exactly = 1) { seasonEpisodeLocalDataSource.getEpisodesByCacheCode(cacheCode) }
            coVerify(exactly = 0) { remoteDataSource.getEpisodes(any(), any()) }
        }

    @Test
    fun `should fetch data from remote when getEpisodes is called and cache is empty`() = runTest {
        val seriesId = 1L
        val seasonNumber = 1
        val cacheCode = getCacheCodeOfEpisodes(seriesId, seasonNumber)
        coEvery { seasonEpisodeLocalDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { seasonEpisodeLocalDataSource.getEpisodesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getEpisodes(seriesId, seasonNumber) } returns listOf(
            episodeRemoteDto
        )
        coEvery { seasonEpisodeLocalDataSource.insertCacheCodeWithEpisodes(any()) } just Runs

        val result = repository.getEpisodes(seriesId, seasonNumber)

        assertThat(result).isEqualTo(listOf(expectedEpisode))
        coVerify(exactly = 1) { remoteDataSource.getEpisodes(seriesId, seasonNumber) }
        coVerify(exactly = 1) { seasonEpisodeLocalDataSource.insertCacheCodeWithEpisodes(any()) }
    }

    @Test
    fun `should return cached genres when getSeriesGenres is called and cache is available`() =
        runTest {
            val cachedGenres = listOf(cachedGenreDto)
            coEvery { localDataSource.getSeriesGenres() } returns cachedGenres

            val result = repository.getSeriesGenres()

            assertThat(result).isEqualTo(listOf(expectedGenre))
            coVerify(exactly = 1) { localDataSource.getSeriesGenres() }
            coVerify(exactly = 0) { remoteDataSource.getSeriesGenres() }
        }

    @Test
    fun `should fetch data from remote when getSeriesGenres is called and cache is empty`() =
        runTest {
            coEvery { localDataSource.getSeriesGenres() } returns emptyList()
            coEvery { remoteDataSource.getSeriesGenres() } returns listOf(genreRemoteDto)
            coEvery { localDataSource.insertSeriesGenres(any()) } just Runs

            val result = repository.getSeriesGenres()

            assertThat(result).isEqualTo(listOf(expectedGenre))
            coVerify(exactly = 1) { remoteDataSource.getSeriesGenres() }
            coVerify(exactly = 1) { localDataSource.insertSeriesGenres(any()) }
        }

    @Test
    fun `should return cached series when getTopRatingSeries is called and cache is available`() =
        runTest {
            val page = 1
            val genreId: Long? = null
            val cacheCode = getCacheCodeOfTopRatedSeries(page, genreId)
            val cachedSeries = listOf(cachedSeriesDto)
            coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
            coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns cachedSeries

            val result = repository.getTopRatingSeries(page, genreId)

            assertThat(result).isEqualTo(listOf(expectedSeries))
            coVerify(exactly = 1) { localDataSource.getSeriesByCacheCode(cacheCode) }
            coVerify(exactly = 0) { remoteDataSource.getTopRatingSeries(any(), any()) }
        }

    @Test
    fun `should fetch data from remote when getTopRatingSeries is called and cache is empty`() =
        runTest {
            val page = 1
            val genreId: Long? = null
            val cacheCode = getCacheCodeOfTopRatedSeries(page, genreId)
            coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
            coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns emptyList()
            coEvery { remoteDataSource.getSeriesGenres() } returns listOf(genreRemoteDto)
            coEvery { remoteDataSource.getTopRatingSeries(page, genreId) } returns listOf(
                seriesRemoteDto
            )
            coEvery { localDataSource.insertCacheCodeWithSeries(any()) } just Runs

            val result = repository.getTopRatingSeries(page, genreId)

            assertThat(result).isEqualTo(listOf(expectedSeries.copy(trailerPath = "", seasonsCount = 1)))
            coVerify(exactly = 1) { remoteDataSource.getTopRatingSeries(page, genreId) }
            coVerify(exactly = 1) { localDataSource.insertCacheCodeWithSeries(any()) }
        }

    @Test
    fun `should return cached series when getSimilarSeries is called and cache is available`() = runTest {
        val seriesId = 1L
        val page = 1
        val cacheCode = getCacheCodeOfSimilarSeries(seriesId, page)
        val cachedSeries = listOf(cachedSeriesDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns cachedSeries

        val result = repository.getSimilarSeries(seriesId, page)

        assertThat(result).isEqualTo(listOf(expectedSeries))
        coVerify(exactly = 1) { localDataSource.getSeriesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getSimilarSeries(any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getSimilarSeries is called and cache is empty`() = runTest {
        val seriesId = 1L
        val page = 1
        val cacheCode = getCacheCodeOfSimilarSeries(seriesId, page)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getSeriesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getSimilarSeries(seriesId, page) } returns listOf(seriesRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithSeries(any()) } just Runs

        val result = repository.getSimilarSeries(seriesId, page)

        assertThat(result).isEqualTo(listOf(expectedSeries.copy(trailerPath = "", seasonsCount = 1)))
        coVerify(exactly = 1) { remoteDataSource.getSimilarSeries(seriesId, page) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithSeries(any()) }
    }

    @Test
    fun `should return cached series when getTrendingSeries is called and cache is available`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfTrendingSeries(page, genreId)
        val cachedSeries = listOf(cachedSeriesDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns cachedSeries

        val result = repository.getTrendingSeries(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedSeries))
        coVerify(exactly = 1) { localDataSource.getSeriesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getTrendingSeries(any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getTrendingSeries is called and cache is empty`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfTrendingSeries(page, genreId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getSeriesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getTrendingSeries(page, genreId) } returns listOf(seriesRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithSeries(any()) } just Runs

        val result = repository.getTrendingSeries(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedSeries.copy(trailerPath = "", seasonsCount = 1)))
        coVerify(exactly = 1) { remoteDataSource.getTrendingSeries(page, genreId) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithSeries(any()) }
    }

    @Test
    fun `should return cached series when getAllSeries is called and cache is available`() = runTest {
        val page = 1
        val genreId: Long? = null
        val sortType: SortType? = null
        val cacheCode = getCacheCodeOfAllSeries(page, genreId, sortType)
        val cachedSeries = listOf(cachedSeriesDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns cachedSeries

        val result = repository.getAllSeries(page, genreId, sortType)

        assertThat(result).isEqualTo(listOf(expectedSeries))
        coVerify(exactly = 1) { localDataSource.getSeriesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getAllSeries(any(), any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getAllSeries is called and cache is empty`() = runTest {
        val page = 1
        val genreId: Long? = null
        val sortType: SortType? = null
        val cacheCode = getCacheCodeOfAllSeries(page, genreId, sortType)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getSeriesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getAllSeries(page, genreId, sortType?.sortBy) } returns listOf(seriesRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithSeries(any()) } just Runs

        val result = repository.getAllSeries(page, genreId, sortType)

        assertThat(result).isEqualTo(listOf(expectedSeries.copy(trailerPath = "", seasonsCount = 1)))
        coVerify(exactly = 1) { remoteDataSource.getAllSeries(page, genreId, sortType?.sortBy) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithSeries(any()) }
    }

    @Test
    fun `should return cached series when getSeriesByQuery is called and cache is available`() = runTest {
        val query = "Test"
        val page = 1
        val cacheCode = getCacheCodeOfSearchedSeries(query, page)
        val cachedSeries = listOf(cachedSeriesDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns cachedSeries

        val result = repository.getSeriesByQuery(query, page)

        assertThat(result).isEqualTo(listOf(expectedSeries))
        coVerify(exactly = 1) { localDataSource.getSeriesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getSeriesByQuery(any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getSeriesByQuery is called and cache is empty`() = runTest {
        val query = "Test"
        val page = 1
        val cacheCode = getCacheCodeOfSearchedSeries(query, page)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getSeriesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getSeriesByQuery(query, page) } returns listOf(seriesRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithSeries(any()) } just Runs

        val result = repository.getSeriesByQuery(query, page)

        assertThat(result).isEqualTo(listOf(expectedSeries.copy(trailerPath = "", seasonsCount = 1)))
        coVerify(exactly = 1) { remoteDataSource.getSeriesByQuery(query, page) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithSeries(any()) }
    }

    @Test
    fun `should return cached series when getSeriesOfArtist is called and cache is available`() = runTest {
        val artistId = 100L
        val cacheCode = getCacheCodeOfSeriesOfArtist(artistId)
        val cachedSeries = listOf(cachedSeriesDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns cachedSeries

        val result = repository.getSeriesOfArtist(artistId)

        assertThat(result).isEqualTo(listOf(expectedSeries))
        coVerify(exactly = 1) { localDataSource.getSeriesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getSeriesOfArtist(any()) }
    }

    @Test
    fun `should fetch data from remote when getSeriesOfArtist is called and cache is empty`() = runTest {
        val artistId = 100L
        val cacheCode = getCacheCodeOfSeriesOfArtist(artistId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getSeriesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getSeriesOfArtist(artistId) } returns listOf(seriesRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithSeries(any()) } just Runs

        val result = repository.getSeriesOfArtist(artistId)

        assertThat(result).isEqualTo(listOf(expectedSeries.copy(trailerPath = "", seasonsCount = 1)))
        coVerify(exactly = 1) { remoteDataSource.getSeriesOfArtist(artistId) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithSeries(any()) }
    }

    @Test
    fun `should throw DomainEmptyResponseException when getSeriesOfArtist is called and remote returns empty`() = runTest {
        val artistId = 100L
        val cacheCode = getCacheCodeOfSeriesOfArtist(artistId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getSeriesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getSeriesOfArtist(artistId) } throws RepoEmptyResponseException()

        assertFailsWith<DomainEmptyResponseException> {
            repository.getSeriesOfArtist(artistId)
        }
        coVerify(exactly = 1) { remoteDataSource.getSeriesOfArtist(artistId) }
        coVerify(exactly = 0) { localDataSource.insertCacheCodeWithSeries(any()) }
    }

    @Test
    fun `should return cached series when getMoreRecommendedSeries is called and cache is available`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfMoreRecommendedSeries(page, genreId)
        val cachedSeries = listOf(cachedSeriesDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns cachedSeries

        val result = repository.getMoreRecommendedSeries(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedSeries))
        coVerify(exactly = 1) { localDataSource.getSeriesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getMoreRecommendedSeries(any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getMoreRecommendedSeries is called and cache is empty`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfMoreRecommendedSeries(page, genreId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getSeriesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getMoreRecommendedSeries(page, genreId) } returns listOf(seriesRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithSeries(any()) } just Runs

        val result = repository.getMoreRecommendedSeries(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedSeries.copy(trailerPath = "", seasonsCount = 1)))
        coVerify(exactly = 1) { remoteDataSource.getMoreRecommendedSeries(page, genreId) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithSeries(any()) }
    }

    @Test
    fun `should throw DomainEmptyResponseException when getMoreRecommendedSeries is called and remote returns empty`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfMoreRecommendedSeries(page, genreId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getSeriesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getMoreRecommendedSeries(page, genreId) } throws RepoEmptyResponseException()

        assertFailsWith<DomainEmptyResponseException> {
            repository.getMoreRecommendedSeries(page, genreId)
        }
        coVerify(exactly = 1) { remoteDataSource.getMoreRecommendedSeries(page, genreId) }
        coVerify(exactly = 0) { localDataSource.insertCacheCodeWithSeries(any()) }
    }

    @Test
    fun `should return cached series when getOnTvSeries is called and cache is available`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfOnTvSeries(page, genreId)
        val cachedSeries = listOf(cachedSeriesDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns cachedSeries

        val result = repository.getOnTvSeries(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedSeries))
        coVerify(exactly = 1) { localDataSource.getSeriesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getOnTvSeries(any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getOnTvSeries is called and cache is empty`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfOnTvSeries(page, genreId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getSeriesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getOnTvSeries(page, genreId) } returns listOf(seriesRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithSeries(any()) } just Runs

        val result = repository.getOnTvSeries(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedSeries.copy(trailerPath = "", seasonsCount = 1)))
        coVerify(exactly = 1) { remoteDataSource.getOnTvSeries(page, genreId) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithSeries(any()) }
    }

    @Test
    fun `should return cached series when getAiringTodaySeries is called and cache is available`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfAiringTodaySeries(page, genreId)
        val cachedSeries = listOf(cachedSeriesDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns cachedSeries

        val result = repository.getAiringTodaySeries(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedSeries))
        coVerify(exactly = 1) { localDataSource.getSeriesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getAiringTodaySeries(any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getAiringTodaySeries is called and cache is empty`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfAiringTodaySeries(page, genreId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getSeriesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getAiringTodaySeries(page, genreId) } returns listOf(seriesRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithSeries(any()) } just Runs

        val result = repository.getAiringTodaySeries(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedSeries.copy(trailerPath = "", seasonsCount = 1)))
        coVerify(exactly = 1) { remoteDataSource.getAiringTodaySeries(page, genreId) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithSeries(any()) }
    }

    @Test
    fun `should return cached series when getFreeToWatchSeries is called and cache is available`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfFreeToWatchSeries(page, genreId)
        val cachedSeries = listOf(cachedSeriesDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns cachedSeries

        val result = repository.getFreeToWatchSeries(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedSeries))
        coVerify(exactly = 1) { localDataSource.getSeriesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getFreeToWatchSeries(any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getFreeToWatchSeries is called and cache is empty`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfFreeToWatchSeries(page, genreId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getSeriesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getFreeToWatchSeries(page, genreId) } returns listOf(seriesRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithSeries(any()) } just Runs

        val result = repository.getFreeToWatchSeries(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedSeries.copy(trailerPath = "", seasonsCount = 1)))
        coVerify(exactly = 1) { remoteDataSource.getFreeToWatchSeries(page, genreId) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithSeries(any()) }
    }

    @Test
    fun `should return cached series when getSeriesByCategory is called and cache is available`() = runTest {
        val genreId = 1L
        val page = 1
        val cacheCode = getCacheCodeOfSeriesByCategory(page, genreId)
        val cachedSeries = listOf(cachedSeriesDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns cachedSeries

        val result = repository.getSeriesByCategory(genreId, page)

        assertThat(result).isEqualTo(listOf(expectedSeries))
        coVerify(exactly = 1) { localDataSource.getSeriesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getSeriesByCategory(any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getSeriesByCategory is called and cache is empty`() = runTest {
        val genreId = 1L
        val page = 1
        val cacheCode = getCacheCodeOfSeriesByCategory(page, genreId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getSeriesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getSeriesByCategory(genreId, page) } returns listOf(seriesRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithSeries(any()) } just Runs

        val result = repository.getSeriesByCategory(genreId, page)

        assertThat(result).isEqualTo(listOf(expectedSeries.copy(trailerPath = "", seasonsCount = 1)))
        coVerify(exactly = 1) { remoteDataSource.getSeriesByCategory(genreId, page) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithSeries(any()) }
    }

    @Test
    fun `should return cached series when getPopularSeries is called and cache is available`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfPopularSeries(page, genreId)
        val cachedSeries = listOf(cachedSeriesDto)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns cachedSeries

        val result = repository.getPopularSeries(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedSeries))
        coVerify(exactly = 1) { localDataSource.getSeriesByCacheCode(cacheCode) }
        coVerify(exactly = 0) { remoteDataSource.getPopularSeries(any(), any()) }
    }

    @Test
    fun `should fetch data from remote when getPopularSeries is called and cache is empty`() = runTest {
        val page = 1
        val genreId: Long? = null
        val cacheCode = getCacheCodeOfPopularSeries(page, genreId)
        coEvery { localDataSource.deleteExpiredCache(any()) } just Runs
        coEvery { localDataSource.getSeriesByCacheCode(cacheCode) } returns emptyList()
        coEvery { remoteDataSource.getSeriesGenres() } returns listOf(genreRemoteDto)
        coEvery { remoteDataSource.getPopularSeries(page, genreId) } returns listOf(seriesRemoteDto)
        coEvery { localDataSource.insertCacheCodeWithSeries(any()) } just Runs

        val result = repository.getPopularSeries(page, genreId)

        assertThat(result).isEqualTo(listOf(expectedSeries.copy(trailerPath = "", seasonsCount = 1)))
        coVerify(exactly = 1) { remoteDataSource.getPopularSeries(page, genreId) }
        coVerify(exactly = 1) { localDataSource.insertCacheCodeWithSeries(any()) }
    }

    private companion object {
        private val seriesRemoteDto = SeriesRemoteDto(
            id = 1L,
            name = "Test Series",
            voteAverage = 8.0f,
            posterPath = "/poster.jpg",
            genreIds = listOf(1L),
            overview = "A great series",
            releaseDate = "2023-01-01"
        )

        private val seriesDetailsRemoteDto = SeriesDetailsRemoteDto(
            id = 1L,
            name = "Test Series",
            voteAverage = 8.0,
            posterPath = "/poster.jpg",
            genres = listOf(GenreDto(id = 1, name = "Drama")),
            overview = "A great series",
            firstAirDate = "2023-01-01",
            numberOfSeasons = 2
        )

        private val expectedGenre = Genre(
            id = 1L,
            name = "Drama"
        )

        private val cachedGenreDto = GenreOfSeriesCacheDto(
            id = 1L,
            name = "Drama",
            cachingTimestamp = System.currentTimeMillis()
        )

        private val cachedSeriesDto = SeriesCacheDto(
            seriesWithoutGenre = SeriesWithoutGenreCacheDto(
                id = 1L,
                title = "Test Series",
                rating = 8.0f,
                posterPath = "/poster.jpg",
                trailerPath = "trailer_key",
                overview = "A great series",
                releaseDate = 1672531200000L,
                seasonsCount = 2,
                cachingTimestamp = System.currentTimeMillis()
            ),
            genres = listOf(cachedGenreDto)
        )

        private val expectedSeries = Series(
            id = 1L,
            title = "Test Series",
            rating = 8.0f,
            posterPath = "/poster.jpg",
            trailerPath = "trailer_key",
            genres = listOf(expectedGenre),
            overview = "A great series",
            releaseDate = 1672531200000L,
            seasonsCount = 2
        )

        private val reviewRemoteDto = ReviewRemoteDto(
            id = "review1",
            author = "John Doe",
            authorDetails = AuthorDetailsDto(
                avatarPath = "/avatar.jpg",
                rating = 8.5
            ),
            content = "Great show!",
            createdAt = "2023-01-01T00:00:00.000Z"
        )

        private val cachedReviewDto = ReviewCacheDto(
            id = "review1",
            author = "John Doe",
            authorPhotoPath = "/avatar.jpg",
            rating = 8.5,
            date = 1672531200000L,
            description = "Great show!",
            cachingTimestamp = System.currentTimeMillis()
        )

        private val expectedReview = Review(
            id = "review1",
            author = "John Doe",
            authorPhotoPath = "/avatar.jpg",
            rating = 8.5,
            date = 1672531200000L,
            description = "Great show!"
        )

        private val seasonRemoteDto = SeasonRemoteDto(
            id = 1L,
            seasonNumber = 1,
            name = "Season 1",
            episodeCount = 10,
            voteAverage = 8.0,
            posterPath = "/season_poster.jpg",
            overview = "First season",
            airDate = "2023-01-01"
        )

        private val cachedSeasonDto = SeasonCacheDto(
            id = 1001L,
            seriesId = 1L,
            seasonNumber = 1,
            seasonName = "Season 1",
            episodesCount = 10,
            rating = 8.0f,
            posterPath = "/season_poster.jpg",
            overview = "First season",
            airDate = 1672531200000L,
            cachingTimestamp = System.currentTimeMillis()
        )

        private val expectedSeason = Season(
            seriesId = 1L,
            seasonNumber = 1,
            seasonName = "Season 1",
            episodesCount = 10,
            rating = 8.0f,
            posterPath = "/season_poster.jpg",
            overview = "First season",
            airDate = 1672531200000L
        )

        private val episodeRemoteDto = EpisodeRemoteDto(
            id = 1L,
            episodeNumber = 1,
            name = "Episode 1",
            runtime = 45,
            voteAverage = 8.0,
            stillPath = "/episode_still.jpg",
            overview = "First episode",
            airDate = "2023-01-01",
            seasonNumber = 1,
            seriesId = 1L
        )

        private val cachedEpisodeDto = EpisodeCacheDto(
            id = 1L,
            episodeNumber = 1,
            episodeName = "Episode 1",
            runtimeMinutes = 45,
            rating = 8.0f,
            photoPath = "/episode_still.jpg",
            seasonNumber = 1,
            seriesId = 1L,
            cachingTimestamp = System.currentTimeMillis()
        )

        private val expectedEpisode = Episode(
            id = 1L,
            episodeNumber = 1,
            episodeName = "Episode 1",
            runtimeMinutes = 45,
            rating = 8.0f,
            photoPath = "/episode_still.jpg",
            seasonNumber = 1,
            seriesId = 1L
        )

        private val genreRemoteDto = GenreDto(
            id = 1,
            name = "Drama"
        )
    }
}