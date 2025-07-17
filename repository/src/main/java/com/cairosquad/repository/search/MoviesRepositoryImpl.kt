package com.cairosquad.repository.search

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review
import com.cairosquad.repository.common.exception.tryToCall
import com.cairosquad.repository.search.data_source.local.DiscoveryDataSource
import com.cairosquad.repository.search.data_source.local.dto.toCacheDto
import com.cairosquad.repository.search.data_source.local.dto.toEntity
import com.cairosquad.repository.search.data_source.remote.RemoteMovieDiscoveryDataSource
import com.cairosquad.repository.search.data_source.remote.dto.toEntity
import java.util.Date

class MoviesRepositoryImpl(
    private val remoteMovieDiscoveryDataSource: RemoteMovieDiscoveryDataSource,
    private val discoveryDataSource: DiscoveryDataSource
) : MoviesRepository {
    override suspend fun getMovie(movieId: Long): Movie {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieReviews(movieId: Long): List<Review> {
        TODO("Not yet implemented")
    }

    override suspend fun getSimilarMovies(movieId: Long): List<Movie> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieTopCast(movieId: Long): List<Artist> {
        TODO("Not yet implemented")
    }

    override suspend fun getPersonalizedMovies(): List<Movie> {
        return tryToCall {
            discoveryDataSource.clearExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
            discoveryDataSource.getPersonalizedMovies()
                .takeIf { it.size >= PAGE_SIZE }?.toEntity()
                ?: remoteMovieDiscoveryDataSource.getPersonalizedMovies().toEntity()
                    .also { result -> discoveryDataSource.cachePersonalizedMovies(result.toCacheDto()) }
        }
    }

    override suspend fun getSuggestedMovies(): List<Movie> {
        return tryToCall {
            discoveryDataSource.clearExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
            discoveryDataSource.getSuggestedMovies()
                .takeIf { it.size >= PAGE_SIZE }?.toEntity()
                ?: remoteMovieDiscoveryDataSource.getSuggestedMovies().toEntity()
                    .also { result -> discoveryDataSource.cacheSuggestedMovies(result.toCacheDto()) }
        }
    }

    private companion object {
        private const val CACHE_EXPIRATION_MILLIS = 3_600_000
        private const val PAGE_SIZE = 20
    }
}