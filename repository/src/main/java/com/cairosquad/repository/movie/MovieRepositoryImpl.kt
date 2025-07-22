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
            remoteMovieDataSource.getMovie(movieId).toEntity()
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

    override suspend fun getPersonalizedMovies(page: Int): List<Movie> {
        return tryToCall {
            discoveryDataSource.clearExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
            discoveryDataSource.getPersonalizedMovies(page)
                .takeIf { it.size >= PAGE_SIZE }?.map { it.toEntity() }
                ?: remoteMovieDiscoveryDataSource.getPersonalizedMovies(page).map { it.toEntity() }
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

    override suspend fun getTopRatingMovies(page: Int): List<Movie> {
        return tryToCall {
            remoteMovieDataSource.getTopRatingMovies(page).map { it.toEntity() }
        }
    }

    override suspend fun getUpcomingMovies(page: Int): List<Movie> {
        return tryToCall {
            remoteMovieDataSource.getUpcomingMovies(page).map { it.toEntity() }
        }
    }

    override suspend fun getNowPlayingMovies(page: Int): List<Movie> {
        return tryToCall {
            remoteMovieDataSource.getNowPlayingMovies(page).map { it.toEntity() }
        }
    }

    override suspend fun getTrendingMovies(page: Int): List<Movie> {
        return tryToCall {
            remoteMovieDataSource.getTrendingMovies(page).map { it.toEntity() }
        }
    }

    override suspend fun getMoreRecommendedMovies(page: Int): List<Movie> {
        return tryToCall {
            remoteMovieDataSource.getMoreRecommendedMovies(page).map { it.toEntity() }
        }
    }

    override suspend fun getFreeToWatchMovies(page: Int): List<Movie> {
        return tryToCall {
            remoteMovieDataSource.getFreeToWatchMovies(page).map { it.toEntity() }
        }
    }

    override suspend fun getMoviesByCategory(
        categoryId: String,
        page: Int
    ): List<Movie> {
        return tryToCall {
            remoteMovieDataSource.getMoviesByCategory(categoryId,page).map { it.toEntity() }
        }
    }

    private companion object {
        private const val CACHE_EXPIRATION_MILLIS = 3_600_000
        private const val PAGE_SIZE = 20
    }
}