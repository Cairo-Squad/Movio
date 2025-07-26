package com.cairosquad.local.cache.series

import com.cairosquad.local.cache.genre.GenreDao
import com.cairosquad.local.cache.request.RequestCachedDao
import com.cairosquad.local.cache.reviews.ReviewDao
import com.cairosquad.repository.series.data_source.local.SeriesLocalDataSource
import com.cairosquad.repository.series.data_source.local.dto.GenreOfSeriesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.RequestSeriesCacheCrossRef
import com.cairosquad.repository.series.data_source.local.dto.RequestWithSeriesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.SeriesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.SeriesGenreCacheCrossRef
import com.cairosquad.repository.utils.sharedDto.local.RequestReviewCacheCrossRef
import com.cairosquad.repository.utils.sharedDto.local.RequestWithReviewsCacheDto
import com.cairosquad.repository.utils.sharedDto.local.ReviewCacheDto

class SeriesLocalDataSourceImpl(
    private val seriesCacheDao: SeriesCacheDao,
    private val requestCachedDao: RequestCachedDao,
    private val genreDao: GenreDao,
    private val reviewDao: ReviewDao
): SeriesLocalDataSource {
    override suspend fun insertRequestWithSeries(requestWithSeries: RequestWithSeriesCacheDto) {
        seriesCacheDao.insertSeriesWithoutGenre(requestWithSeries.series.map { it.seriesWithoutGenre })
        genreDao.insertSeriesGenres(genres = requestWithSeries.series.map { it.genres }.flatten().toSet().toList())
        seriesCacheDao.insertSeriesGenreCacheCrossRef(
            requestWithSeries.series.map { series ->
                series.genres.map { genre ->
                    SeriesGenreCacheCrossRef(series.seriesWithoutGenre.id, genre.id)
                }
            }.flatten()
        )
        requestCachedDao.insertRequestCache(requestWithSeries.request)
        seriesCacheDao.insertRequestSeriesCacheCrossRef(
            RequestSeriesCacheCrossRef.fromRequestAndSeriesList(
                request = requestWithSeries.request,
                seriesList = requestWithSeries.series
            )
        )
    }

    override suspend fun getSeriesByRequest(request: String): List<SeriesCacheDto> {
        return seriesCacheDao.getSeriesByRequest(request)?.series ?: emptyList()
    }

    override suspend fun insertSeriesGenres(genres: List<GenreOfSeriesCacheDto>) {
        genreDao.insertSeriesGenres(genres)
    }

    override suspend fun getSeriesGenres(): List<GenreOfSeriesCacheDto> {
        return genreDao.getSeriesGenres()
    }

    override suspend fun getSeriesReviewsByRequest(request: String): List<ReviewCacheDto> {
        return reviewDao.getReviewsByRequest(request)?.reviews ?: emptyList()
    }

    override suspend fun insertRequestWithReviews(requestWithReviewsCacheDto: RequestWithReviewsCacheDto) {
        requestCachedDao.insertRequestCache(requestWithReviewsCacheDto.request)
        reviewDao.insertReviews(requestWithReviewsCacheDto.reviews)
        reviewDao.insertRequestReviewCacheCrossRef(
            RequestReviewCacheCrossRef.fromRequestAndReviewList(
                request = requestWithReviewsCacheDto.request,
                reviews = requestWithReviewsCacheDto.reviews
            )
        )
    }

    override suspend fun deleteExpiredCache(timestamp: Long) {
        requestCachedDao.deleteExpiredRequestCache(timestamp)

        seriesCacheDao.deleteExpiredSeriesWithoutGenreCache(timestamp)
        seriesCacheDao.deleteUnwantedRequestSeriesCacheCrossRef()

        genreDao.deleteExpiredSeriesGenreCache(timestamp)
        seriesCacheDao.deleteUnwantedSeriesGenreCacheCrossRef()

        reviewDao.deleteExpiredReviewCache(timestamp)
        reviewDao.deleteUnwantedRequestReviewCacheCrossRef()
    }
}