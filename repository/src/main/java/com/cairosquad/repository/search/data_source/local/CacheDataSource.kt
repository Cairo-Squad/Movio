package com.cairosquad.repository.search.data_source.local

import com.cairosquad.repository.artists.dto.ArtistMovieCachedDto
import com.cairosquad.repository.artists.dto.ArtistSeriesCachedDto
import com.cairosquad.repository.search.data_source.local.dto.ArtistCacheDto
import com.cairosquad.repository.search.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.search.data_source.local.dto.SeriesCacheDto

interface CacheDataSource {
    suspend fun getCachedMovies(): List<MovieCacheDto>
    suspend fun getCachedMovies(id: Long): List<MovieCacheDto>
    suspend fun getCachedMovies(query: String,page:Int): List<MovieCacheDto>
    suspend fun cacheMovies(movies: List<MovieCacheDto>)

    suspend fun getCachedSeries(): List<SeriesCacheDto>
    suspend fun getCachedSeries(id: Long): List<SeriesCacheDto>
    suspend fun getCachedSeries(query: String, page: Int): List<SeriesCacheDto>
    suspend fun cacheSeries(series: List<SeriesCacheDto>)

    suspend fun getCachedArtists(): List<ArtistCacheDto>
    suspend fun getCachedArtists(id: Long): ArtistCacheDto
    suspend fun getCachedArtists(query: String, page: Int): List<ArtistCacheDto>
    suspend fun cacheArtist(artists: List<ArtistCacheDto>)

    suspend fun cacheArtistMovies(artistMovieCachedDto: List<ArtistMovieCachedDto>)
    suspend fun getCachedArtistMovies(artistId: Long): List<MovieCacheDto>

    suspend fun cacheArtistSeries(artistSeriesCachedDto: List<ArtistSeriesCachedDto>)
    suspend fun getCachedArtistSeries(artistId: Long): List<SeriesCacheDto>

    suspend fun clearExpiredCache(expirationTime: Long)
}
