package com.cairosquad.remote.series

import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.movie.data_source.remote.dto.GenreDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.series.data_source.remote.SeriesRemoteDataSource
import com.cairosquad.repository.series.data_source.remote.dto.EpisodeRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeasonRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesDetailsRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import java.time.LocalDate
import javax.inject.Inject

class SeriesRemoteDataSourceImpl @Inject constructor(
    private val apiService: SeriesApiService
) : SeriesRemoteDataSource {
    override suspend fun getSeriesById(id: Long): SeriesDetailsRemoteDto {
        return safeCallApi { apiService.getSeriesById(id) }
    }

    override suspend fun getSeriesReviews(
        seriesId: Long,
        page: Int
    ): List<ReviewRemoteDto> {
        return safeCallApi { apiService.getSeriesReviews(seriesId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSimilarSeries(
        seriesId: Long,
        page: Int
    ): List<SeriesRemoteDto> {
        return safeCallApi { apiService.getSimilarSeries(seriesId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesOfArtist(artistId: Long): List<SeriesRemoteDto> {
        return safeCallApi { apiService.getSeriesOfArtist(artistId) }
            .series.filter { it.id != null }
    }

    override suspend fun getSeriesSeasons(seriesId: Long): List<SeasonRemoteDto> {
        return safeCallApi { apiService.getSeriesSeasons(seriesId) }
            .seasons?.filterNotNull() ?: emptyList()
    }

    override suspend fun getEpisodes(
        seriesId: Long,
        seasonNumber: Int
    ): List<EpisodeRemoteDto> {
        return safeCallApi { apiService.getEpisodes(seriesId, seasonNumber) }
            .episodes ?: emptyList()
    }

    override suspend fun getVideoKey(seriesId: Long): String {
        return safeCallApi { apiService.getVideoKey(seriesId).getVideoKey() ?: "" }
    }

    override suspend fun getTopRatingSeries(page: Int, genreId: Long?): List<SeriesRemoteDto> {
        return safeCallApi { apiService.getTopRatingSeries(page, genreId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoreRecommendedSeries(
        page: Int,
        genreId: Long?
    ): List<SeriesRemoteDto> {
        return safeCallApi { apiService.getMoreRecommendedSeries(page, withGenres = genreId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getOnTvSeries(page: Int, genreId: Long?): List<SeriesRemoteDto> {
        val today = LocalDate.now()
        val oneWeekAgo = today.minusDays(7)
        return safeCallApi {
            apiService.getOnTvSeries(
                page = page,
                withGenres = genreId,
                minDate = oneWeekAgo.toString(),
                maxDate = today.toString()
            )
        }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getAiringTodaySeries(page: Int, genreId: Long?): List<SeriesRemoteDto> {
        val today = LocalDate.now()

        val minDate = today.toString()
        val maxDate = today.toString()
        return safeCallApi {
            apiService.getAiringTodaySeries(
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
            apiService.getTrendingSeries(
                page, genreId,
                minDate = thirtyDaysAgo.toString(),
                maxDate = today.toString()
            )
        }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getFreeToWatchSeries(page: Int, genreId: Long?): List<SeriesRemoteDto> {
        return safeCallApi { apiService.getFreeToWatchSeries(page, withGenres = genreId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesByCategory(
        genreId: Long,
        page: Int
    ): List<SeriesRemoteDto> {
        return safeCallApi { apiService.getSeriesByCategory(genreId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesGenres(): List<GenreDto> {
        return safeCallApi { apiService.getSeriesGenres() }
            .genres?.filterNotNull().orEmpty()
    }

    override suspend fun getPopularSeries(page: Int, genreId: Long?): List<SeriesRemoteDto> {
        return safeCallApi { apiService.getPopularSeries(page, genreId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getAllSeries(
        page: Int,
        genreId: Long?,
        sortBy: String?
    ): List<SeriesRemoteDto> {
        return safeCallApi { apiService.getAllSeries(page, genreId, sortBy) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesByQuery(query: String, page: Int): List<SeriesRemoteDto> {
        return safeCallApi { apiService.getSeriesByQuery(query, page) }
            .results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }
}
