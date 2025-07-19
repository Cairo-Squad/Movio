package com.cairosquad.repository.movie

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review
import com.cairosquad.repository.movie.data_source.remote.RemoteMovieDataSource
import com.cairosquad.repository.search.data_source.local.DiscoveryDataSource
import com.cairosquad.repository.search.data_source.local.dto.toCacheDto
import com.cairosquad.repository.search.data_source.local.dto.toEntity
import com.cairosquad.repository.search.data_source.remote.RemoteMovieDiscoveryDataSource
import com.cairosquad.repository.search.data_source.remote.dto.toEntity
import com.cairosquad.repository.utils.mappers.tryToCall
import java.util.Date

class MovieRepositoryImpl(
    private val remoteMovieDiscoveryDataSource: RemoteMovieDiscoveryDataSource,
    private val discoveryDataSource: DiscoveryDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource
) : MoviesRepository {
    override suspend fun getMovie(movieId: Long): Movie {
        return tryToCall {
            remoteMovieDataSource.getMovie(movieId).toEntity(
                remoteMovieDataSource.getVideoKey(movieId) ?: ""
            )
        }
    }

    override suspend fun getMovieReviews(movieId: Long, page: Int): List<Review> {
        return tryToCall {
            remoteMovieDataSource.getMovieReviews(movieId, page).map { it.toEntity() }
        }
    }

    override suspend fun getSimilarMovies(movieId: Long, page: Int): List<Movie> {
        return tryToCall {
            remoteMovieDataSource.getSimilarMovies(movieId, page).map { it.toEntity() }
        }
    }

    override suspend fun getMovieTopCast(movieId: Long, page: Int): List<Artist> {
        return tryToCall {
            remoteMovieDataSource.getMovieTopCast(movieId, page).map { it.toEntity() }
        }
    }

    override suspend fun getPersonalizedMovies(): List<Movie> {
        return tryToCall {
            discoveryDataSource.clearExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
            discoveryDataSource.getPersonalizedMovies()
                .takeIf { it.size >= PAGE_SIZE }?.map { it.toEntity() }
                ?: remoteMovieDiscoveryDataSource.getPersonalizedMovies().map { it.toEntity() }
                    .also { result ->
                        discoveryDataSource.cachePersonalizedMovies(
                            result.toCacheDto(
                                "ELSAYEDMAGDY",
                                1
                            )
                        )
                    }

        }
    }

    override suspend fun getSuggestedMovies(): List<Movie> {
        return tryToCall {
            discoveryDataSource.clearExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
            discoveryDataSource.getSuggestedMovies()
                .takeIf { it.size >= PAGE_SIZE }?.map { it.toEntity() }
                ?: remoteMovieDiscoveryDataSource.getSuggestedMovies().map { it.toEntity() }
                    .also { result ->
                        discoveryDataSource.cacheSuggestedMovies(
                            result.toCacheDto(
                                "ELSAYEDMAGDY",
                                1
                            )
                        )
                    }
        }
    }

    private companion object {
        private const val CACHE_EXPIRATION_MILLIS = 3_600_000
        private const val PAGE_SIZE = 20
    }
}