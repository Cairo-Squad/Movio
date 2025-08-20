package com.cairosquad.local.cache.series

import com.cairosquad.local.cache.cacheCode.CacheCodeDao
import com.cairosquad.local.cache.genre.GenreDao
import com.cairosquad.local.cache.reviews.ReviewDao
import com.cairosquad.repository.series.data_source.local.SeriesLocalDataSource
import com.cairosquad.repository.series.data_source.local.dto.CacheCodeSeriesCacheCrossRef
import com.cairosquad.repository.series.data_source.local.dto.CacheCodeWithSeriesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.GenreOfSeriesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.SeriesCacheDto
import com.cairosquad.repository.series.data_source.local.dto.SeriesGenreCacheCrossRef
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeReviewCacheCrossRef
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeWithReviewsCacheDto
import com.cairosquad.repository.utils.sharedDto.local.ReviewCacheDto
import javax.inject.Inject

class SeriesLocalDataSourceImpl @Inject constructor(
    private val seriesCacheDao: SeriesCacheDao,
    private val cacheCodeDao: CacheCodeDao,
    private val genreDao: GenreDao,
    private val reviewDao: ReviewDao
) : SeriesLocalDataSource {
    override suspend fun insertCacheCodeWithSeries(cacheCodeWithSeries: CacheCodeWithSeriesCacheDto) {
        seriesCacheDao.insertSeriesWithoutGenre(
            cacheCodeWithSeries.series
                .map { it.seriesWithoutGenre }
        )
        genreDao.insertSeriesGenres(
            genres = cacheCodeWithSeries.series
                .map { it.genres }
                .flatten()
                .distinctBy { it.id }
        )
        seriesCacheDao.insertCrossRefForSeriesAndGenreCache(
            cacheCodeWithSeries.series.map { series ->
                SeriesGenreCacheCrossRef.fromSeries(series)
            }.flatten()
        )
        cacheCodeDao.insertCacheCode(
            cacheCodeWithSeries.cacheCode
        )
        seriesCacheDao.insertCrossRefForCacheCodeAndSeriesCache(
            CacheCodeSeriesCacheCrossRef.fromRequestAndSeriesList(
                cacheCode = cacheCodeWithSeries.cacheCode,
                seriesList = cacheCodeWithSeries.series
            )
        )
    }

    override suspend fun getSeriesByCacheCode(cacheCode: String): List<SeriesCacheDto> {
        return seriesCacheDao.getSeriesByCacheCode(cacheCode)?.series ?: emptyList()
    }

    override suspend fun insertSeriesGenres(genres: List<GenreOfSeriesCacheDto>) {
        genreDao.insertSeriesGenres(genres)
    }

    override suspend fun getSeriesGenres(): List<GenreOfSeriesCacheDto> {
        return genreDao.getSeriesGenres()
    }

    override suspend fun getSeriesGenresByLanguage(language: String): List<GenreOfSeriesCacheDto> {
        return genreDao.getSeriesGenresByLanguage(language)
    }

    override suspend fun getSeriesReviewsByCacheCode(cacheCode: String): List<ReviewCacheDto> {
        return reviewDao.getReviewsByCacheCode(cacheCode)?.reviews ?: emptyList()
    }

    override suspend fun insertCacheCodeWithReviews(cacheCodeWithReviewsCacheDto: CacheCodeWithReviewsCacheDto) {
        cacheCodeDao.insertCacheCode(cacheCodeWithReviewsCacheDto.cacheCode)
        reviewDao.insertReviews(cacheCodeWithReviewsCacheDto.reviews)
        reviewDao.insertRequestReviewCacheCrossRef(
            CacheCodeReviewCacheCrossRef.fromRequestAndReviewList(
                cacheCode = cacheCodeWithReviewsCacheDto.cacheCode,
                reviews = cacheCodeWithReviewsCacheDto.reviews
            )
        )
    }

    override suspend fun deleteExpiredCache(timestamp: Long) {
        cacheCodeDao.deleteExpiredCacheCode(timestamp)

        seriesCacheDao.deleteExpiredSeriesWithoutGenreCache(timestamp)
        seriesCacheDao.deleteCrossRefForNonExistingCacheCodeAndSeriesCache()

        genreDao.deleteExpiredGenreOfSeriesCache(timestamp)
        seriesCacheDao.deleteCrossRefForNonExistingSeriesAndGenreCache()

        reviewDao.deleteExpiredReviewCache(timestamp)
        reviewDao.deleteCrossRefForNonexistingRequestReviewCache()
    }
}