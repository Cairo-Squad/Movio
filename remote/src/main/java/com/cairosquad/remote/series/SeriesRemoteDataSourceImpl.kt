package com.cairosquad.remote.series

import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.artists.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.GenreDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.series.data_source.remote.SeriesRemoteDataSource
import com.cairosquad.repository.series.data_source.remote.dto.EpisodeRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeasonRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesDetailsRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import java.time.LocalDate

class SeriesRemoteDataSourceImpl(
    private val seriesApiService: SeriesApiService
) : SeriesRemoteDataSource {
    override suspend fun getSeriesById(id: Long): SeriesDetailsRemoteDto {
        return safeCallApi { seriesApiService.getSeriesById(id) }
    }

    override suspend fun getSeriesReviews(
        seriesId: Long,
        page: Int
    ): List<ReviewRemoteDto> {
        return safeCallApi { seriesApiService.getSeriesReviews(seriesId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSimilarSeries(
        seriesId: Long,
        page: Int
    ): List<SeriesRemoteDto> {
        return safeCallApi { seriesApiService.getSimilarSeries(seriesId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesTopCast(
        seriesId: Long,
        page: Int
    ): List<ArtistRemoteDto> {
        return safeCallApi { seriesApiService.getSeriesTopCast(seriesId, page) }
            .cast?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesSeasons(seriesId: Long): List<SeasonRemoteDto> {
        return safeCallApi { seriesApiService.getSeriesSeasons(seriesId) }
            .seasons?.filterNotNull() ?: emptyList()
    }

    override suspend fun getEpisodes(
        seriesId: Long,
        seasonNumber: Int
    ): List<EpisodeRemoteDto> {
        return safeCallApi { seriesApiService.getEpisodes(seriesId, seasonNumber) }
            .episodes ?: emptyList()
    }

    override suspend fun getVideoKey(seriesId: Long): String {
        return safeCallApi { seriesApiService.getVideoKey(seriesId).getVideoKey() ?: "" }
    }

    override suspend fun getTopRatingSeries(page: Int, genreId: Long?): List<SeriesRemoteDto> {
        return safeCallApi { seriesApiService.getTopRatingSeries(page, genreId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoreRecommendedSeries(
        page: Int,
        genreId: Long?
    ): List<SeriesRemoteDto> {
        return safeCallApi { seriesApiService.getMoreRecommendedSeries(page, withGenres = genreId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getOnTvSeries(page: Int, genreId: Long?): List<SeriesRemoteDto> {
        val today = LocalDate.now()
        val oneWeekAgo = today.minusDays(7)
        return safeCallApi {
            seriesApiService.getOnTvSeries(
                page, genreId,
                minDate = oneWeekAgo.toString(), maxDate = today.toString()
            )
        }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getAiringTodaySeries(page: Int, genreId: Long?): List<SeriesRemoteDto> {
        val today = LocalDate.now()

        val minDate = today.toString()
        val maxDate = today.toString()
        return safeCallApi {
            seriesApiService.getAiringTodaySeries(
                page, genreId,
                minDate = minDate, maxDate = maxDate
            )
        }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getTrendingSeries(page: Int, genreId: Long?): List<SeriesRemoteDto> {
        val today = LocalDate.now()
        val thirtyDaysAgo = today.minusDays(30)
        return safeCallApi {
            seriesApiService.getTrendingSeries(
                page, genreId,
                minDate = thirtyDaysAgo.toString(),
                maxDate = today.toString()
            )
        }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getFreeToWatchSeries(page: Int, genreId: Long?): List<SeriesRemoteDto> {
        return safeCallApi { seriesApiService.getFreeToWatchSeries(page, withGenres = genreId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesByCategory(
        genreId: Long,
        page: Int
    ): List<SeriesRemoteDto> {
        return safeCallApi { seriesApiService.getSeriesByCategory(genreId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesGenres(): List<GenreDto> {
        return safeCallApi { seriesApiService.getSeriesGenres() }
            .genres?.filterNotNull().orEmpty()
    }

    override suspend fun getPopularSeries(page: Int, genreId: Long?): List<SeriesRemoteDto> {
        return safeCallApi { seriesApiService.getPopularSeries(page, genreId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getAllSeries(
        page: Int,
        genreId: Long?,
        sortBy: String?
    ): List<SeriesRemoteDto> {
        return safeCallApi { seriesApiService.getAllSeries(page, genreId, sortBy) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesByQuery(query: String, page: Int): List<SeriesRemoteDto> {
        return safeCallApi { seriesApiService.getSeriesByQuery(query, page) }
            .results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }
}
