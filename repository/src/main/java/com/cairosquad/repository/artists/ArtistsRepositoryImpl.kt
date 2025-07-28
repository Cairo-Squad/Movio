package com.cairosquad.repository.artists

import com.cairosquad.domain.repository.ArtistsRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.artists.data_source.ArtistsRemoteDataSource
import com.cairosquad.repository.search.data_source.local.CacheDataSource
import com.cairosquad.repository.search.data_source.local.dto.toCacheDto
import com.cairosquad.repository.search.data_source.local.dto.toCacheDtoList
import com.cairosquad.repository.search.data_source.local.dto.toEntity
import com.cairosquad.repository.search.data_source.local.dto.toEntityList
import com.cairosquad.repository.search.data_source.remote.dto.toEntity
import com.cairosquad.repository.search.data_source.remote.dto.toEntityList
import com.cairosquad.repository.utils.mappers.tryToCall
import java.util.Date

class ArtistsRepositoryImpl(
    private val artistsRemoteDataSource: ArtistsRemoteDataSource,
    private val cacheDataSource: CacheDataSource,
) : ArtistsRepository {
    override suspend fun getArtist(artistId: Long): Artist {
        return try {
            cacheDataSource.clearExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
            cacheDataSource.getCachedArtists(id = artistId).toEntity()
        } catch (_: IllegalStateException) {
            tryToCall {
                artistsRemoteDataSource.getArtist(artistId).toEntity()
                    .also { cacheDataSource.cacheArtist(listOf(it.toCacheDto(
                        "ELSAYEDMAGDY",
                        1
                    ))) }
            }
        }


    }

    override suspend fun getMoviesOfArtist(artistId: Long): List<Movie> {
        return tryToCall {
            cacheDataSource.clearExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
            cacheDataSource.getCachedArtistMovies(artistId = artistId)
                .takeIf { it.isNotEmpty() }?.toEntityList()
                ?: artistsRemoteDataSource.getMoviesOfArtist(artistId).toEntityList()
                    .also { cacheDataSource.cacheMovies(it.toCacheDtoList(
                        "ELSAYEDMAGDY",
                        1
                    )) }
                    .also { cacheDataSource.cacheArtistMovies(it.toArtistMoviesCachedDtoList(artistId)) }
        }
    }

    override suspend fun getSeriesOfArtist(artistId: Long): List<Series> {
        return tryToCall {
            cacheDataSource.clearExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
            cacheDataSource.getCachedArtistSeries(artistId = artistId)
                .takeIf { it.isNotEmpty() }?.toEntityList()
                ?: artistsRemoteDataSource.getSeriesOfArtist(artistId).toEntityList()
                    .also { cacheDataSource.cacheSeries(it.toCacheDtoList(
                        "ELSAYEDMAGDY",
                        1
                    )) }
                    .also { cacheDataSource.cacheArtistSeries(it.toArtistSeriesCachedDtoList(artistId)) }
        }
    }

    private companion object {
        private const val CACHE_EXPIRATION_MILLIS = 3_600_000
    }
}