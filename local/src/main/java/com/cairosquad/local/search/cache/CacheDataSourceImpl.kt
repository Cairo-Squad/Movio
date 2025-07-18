package com.cairosquad.local.search.cache

import com.cairosquad.local.search.cache.dao.CacheDao
import com.cairosquad.repository.artists.dto.ArtistMovieCachedDto
import com.cairosquad.repository.artists.dto.ArtistSeriesCachedDto
import com.cairosquad.repository.search.data_source.local.dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.dto.SeriesCacheDto
import com.cairosquad.repository.search.data_source.local.CacheDataSource

class CacheDataSourceImpl(
    private val cacheDao: CacheDao
): CacheDataSource {
    override suspend fun getCachedMovies() = cacheDao.getCachedMovies()

    override suspend fun getCachedMovies(id: Long) = cacheDao.getCachedMovies(id)

    override suspend fun getCachedMovies(query: String) = cacheDao.getCachedMovies(query)

    override suspend fun cacheMovies(movies: List<MovieCacheDto>) = cacheDao.cacheMovies(movies)


    override suspend fun getCachedSeries() = cacheDao.getCachedSeries()

    override suspend fun getCachedSeries(id: Long) = cacheDao.getCachedSeries(id)

    override suspend fun getCachedSeries(query: String) = cacheDao.getCachedSeries(query)

    override suspend fun cacheSeries(series: List<SeriesCacheDto>) = cacheDao.cacheSeries(series)


    override suspend fun getCachedArtists() = cacheDao.getCachedArtist()

    override suspend fun getCachedArtists(id: Long) = cacheDao.getCachedArtist(id)

    override suspend fun getCachedArtists(query: String) = cacheDao.getCachedArtist(query)

    override suspend fun cacheArtist(artists: List<ArtistCacheDto>) = cacheDao.cacheArtist(artists)

    override suspend fun cacheArtistMovies(artistMovieCachedDto: List<ArtistMovieCachedDto>) {
        cacheDao.cacheArtistMovies(artistMovieCachedDto)
    }

    override suspend fun getCachedArtistMovies(artistId: Long): List<MovieCacheDto> {
        return cacheDao.getCachedArtistMovies(artistId)
    }

    override suspend fun cacheArtistSeries(artistSeriesCachedDto: List<ArtistSeriesCachedDto>) {
        cacheDao.cacheArtistSeries(artistSeriesCachedDto)
    }

    override suspend fun getCachedArtistSeries(artistId: Long): List<SeriesCacheDto> {
        return cacheDao.getCachedArtistSeries(artistId)
    }

    override suspend fun clearExpiredCache(expirationTime: Long) {
        cacheDao.deleteExpiredArtistMoviesCache(expirationTime)
        cacheDao.deleteExpiredArtistSeriesCache(expirationTime)
        cacheDao.deleteExpiredMoviesCache(expirationTime)
        cacheDao.deleteExpiredSeriesCache(expirationTime)
        cacheDao.deleteExpiredArtistCache(expirationTime)
    }
}