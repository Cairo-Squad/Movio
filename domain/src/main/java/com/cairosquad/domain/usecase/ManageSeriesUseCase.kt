package com.cairosquad.domain.usecase

import com.cairosquad.domain.model.SortType
import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Episode
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Review
import com.cairosquad.entity.Season
import com.cairosquad.entity.Series

class ManageSeriesUseCase(
    private val seriesRepository: SeriesRepository,
    private val searchRepository: SearchRepository
) {
    suspend fun getSeriesByQuery(query: String, page: Int): List<Series> {
        return seriesRepository.getSeriesByQuery(query, page).also {
            searchRepository.addQuery(query)
        }
    }

    suspend fun getAiringTodaySeries(page: Int, genreId: Long? = null): List<Series> {
        return seriesRepository.getAiringTodaySeries(page, genreId)
    }

    suspend fun getAllSeries(
        page: Int,
        genreId: Long? = null,
        sortType: SortType? = null
    ): List<Series> {
        return seriesRepository.getAllSeries(page, genreId, sortType)
    }

    suspend fun getFreeToWatchSeries(page: Int, genreId: Long? = null): List<Series> {
        return seriesRepository.getFreeToWatchSeries(page, genreId)
    }

    suspend fun getMoreRecommendedSeries(page: Int, genreId: Long? = null): List<Series> {
        return seriesRepository.getMoreRecommendedSeries(page, genreId)
    }

    suspend fun getOnTvSeries(page: Int, genreId: Long? = null): List<Series> {
        return seriesRepository.getOnTvSeries(page, genreId)
    }

    suspend fun getPopularSeries(page: Int, genreId: Long? = null): List<Series> {
        return seriesRepository.getPopularSeries(page, genreId)
    }

    suspend fun getSeriesByCategory(
        genreId: Long,
        page: Int,
    ): List<Series> {
        return seriesRepository.getSeriesByCategory(genreId, page)
    }

    suspend fun getSeriesById(id: Long): Series {
        return seriesRepository.getSeriesById(id)
    }

    suspend fun getSeriesReviews(seriesId: Long, page: Int): List<Review> {
        return seriesRepository.getSeriesReviews(seriesId, page)
    }

    suspend fun getSeriesSeasons(seriesId: Long): List<Season> {
        return seriesRepository.getSeriesSeasons(seriesId)
    }

    suspend fun getEpisodes(seriesId: Long, seasonNumber: Int): List<Episode> {
        return seriesRepository.getEpisodes(seriesId, seasonNumber)
    }

    suspend fun getSimilarSeries(seriesId: Long, page: Int): List<Series> {
        return seriesRepository.getSimilarSeries(seriesId, page)
    }

    suspend fun getSeriesTopCast(seriesId: Long, page: Int): List<Artist> {
        return seriesRepository.getSeriesTopCast(seriesId, page)
    }

    suspend fun getSeriesGenres(): List<Genre> {
        return seriesRepository.getSeriesGenres()
    }

    suspend fun getTopRatingSeries(page: Int, genreId: Long? = null): List<Series> {
        return seriesRepository.getTopRatingSeries(page, genreId)
    }

    suspend fun getTrendingSeries(page: Int, genreId: Long? = null): List<Series> {
        return seriesRepository.getTrendingSeries(page, genreId)
    }
}