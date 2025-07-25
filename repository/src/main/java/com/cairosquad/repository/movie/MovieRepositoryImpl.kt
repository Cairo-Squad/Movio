package com.cairosquad.repository.movie

import com.cairosquad.domain.model.SortType
import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review
import com.cairosquad.repository.movie.data_source.local.MoviesCacheDataSource
import com.cairosquad.repository.movie.data_source.local.dto.toEntityList
import com.cairosquad.repository.movie.data_source.local.dto.toRequestWithMoviesCacheDto
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
    private val remoteMovieDataSource: RemoteMovieDataSource,
    private val moviesCacheDataSource: MoviesCacheDataSource
) : MoviesRepository {
    override suspend fun getMovie(movieId: Long): Movie {
        moviesCacheDataSource.deleteExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
        return moviesCacheDataSource
            .getMoviesByRequest(request = getRequestOfMovieCache(movieId))
            .toEntityList()
            .firstOrNull()
            ?: tryToCall {
                remoteMovieDataSource.getMovie(movieId).toEntity(
                    remoteMovieDataSource.getVideoKey(movieId)
                )
            }.also { movie ->
                moviesCacheDataSource.cacheRequestWithMovies(
                    listOf(movie).toRequestWithMoviesCacheDto(request = "movie/${movieId}")
                )
            }
    }

    override suspend fun getMovieReviews(movieId: Long, page: Int): List<Review> {
        return tryToCall {
            remoteMovieDataSource.getMovieReviews(movieId, page).map { it.toEntity() }
        }
    }

    override suspend fun getSimilarMovies(movieId: Long, page: Int): List<Movie> {
        moviesCacheDataSource.deleteExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
        return moviesCacheDataSource
            .getMoviesByRequest(request = getRequestOfSimilarMoviesCache(movieId, page))
            .toEntityList()
            .takeIf { it.isNotEmpty() }
            ?: tryToCall {
                remoteMovieDataSource.getSimilarMovies(movieId, page)
                    .map { it.toEntity() }
                    .also { movies ->
                        moviesCacheDataSource.cacheRequestWithMovies(
                            movies.toRequestWithMoviesCacheDto(
                                request = getRequestOfSimilarMoviesCache(movieId, page)
                            )
                        )
                    }
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

    override suspend fun getTopRatingMovies(page: Int, genreId: String?): List<Movie> {
        moviesCacheDataSource.deleteExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
        return moviesCacheDataSource
            .getMoviesByRequest(request = getRequestOfTopRatedMoviesCache(page, genreId))
            .toEntityList()
            .takeIf { it.isNotEmpty() }
            ?: tryToCall {
                remoteMovieDataSource.getTopRatingMovies(page,genreId)
                    .map { it.toEntity() }
                    .also { movies ->
                        moviesCacheDataSource.cacheRequestWithMovies(
                            movies.toRequestWithMoviesCacheDto(
                                request = getRequestOfTopRatedMoviesCache(page, genreId)
                            )
                        )
                    }
            }
    }

    override suspend fun getUpcomingMovies(page: Int,categoryId: String?): List<Movie> {
        return tryToCall {
            remoteMovieDataSource.getUpcomingMovies(page,categoryId).map { it.toEntity() }
        }
    }

    override suspend fun getNowPlayingMovies(page: Int,categoryId: String?): List<Movie> {
        return tryToCall {
            remoteMovieDataSource.getNowPlayingMovies(page,categoryId).map { it.toEntity() }
        }
    }

    override suspend fun getTrendingMovies(page: Int,categoryId: String?): List<Movie> {
        return tryToCall {
            val genres = remoteMovieDataSource.getMoviesGenres().map { it.toEntity() }
            remoteMovieDataSource.getTrendingMovies(page,categoryId).map { it.toEntity(genres) }
        }
    }

    override suspend fun getMoreRecommendedMovies(page: Int,categoryId: String?): List<Movie> {
        return tryToCall {
            remoteMovieDataSource.getMoreRecommendedMovies(page,categoryId).map { it.toEntity() }
        }
    }

    override suspend fun getFreeToWatchMovies(page: Int,categoryId: String?): List<Movie> {
        return tryToCall {
            remoteMovieDataSource.getFreeToWatchMovies(page,categoryId).map { it.toEntity() }
        }
    }

    override suspend fun getMoviesByCategory(
        page: Int,
        categoryId: String
    ): List<Movie> {
        return tryToCall {
            remoteMovieDataSource.getMoviesByCategory(categoryId,page).map { it.toEntity() }
        }
    }

    override suspend fun getMoviesGenres(): List<Genre> {
        return tryToCall {
            remoteMovieDataSource.getMoviesGenres().map { it.toEntity() }
        }
    }

    override suspend fun getPopularMovies(page: Int,categoryId: String?): List<Movie> {
        return tryToCall {
            val genres = remoteMovieDataSource.getMoviesGenres().map { it.toEntity() }
            remoteMovieDataSource.getPopularMovies(page,categoryId).map { it.toEntity(genres) }
        }
    }

    override suspend fun getAllMovies(page: Int,categoryId: String?,sortType: SortType?): List<Movie> {
        return tryToCall {
            val genres = remoteMovieDataSource.getMoviesGenres().map { it.toEntity() }

            remoteMovieDataSource.getAllMovies(
                page,
                categoryId,
                sortType?.sortBy
            ).map { it.toEntity(genres) }
        }
    }

    private companion object {
        private const val CACHE_EXPIRATION_MILLIS = 3_600_000
        private const val PAGE_SIZE = 20
        private fun getRequestOfMovieCache(movieId: Long) = "movies/movieId = $movieId"
        private fun getRequestOfTopRatedMoviesCache(page: Int, genreId: String?) = "movies/tobRated/page = $page/genreId = $genreId"
        private fun getRequestOfSimilarMoviesCache(movieId: Long, page: Int) = "movies/tobRated/movieId = $movieId/page = $page"
    }
}