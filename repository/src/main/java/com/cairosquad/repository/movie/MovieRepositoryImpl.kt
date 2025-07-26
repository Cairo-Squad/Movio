package com.cairosquad.repository.movie

import com.cairosquad.domain.model.SortType
import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review
import com.cairosquad.repository.movie.data_source.local.MoviesCacheDataSource
import com.cairosquad.repository.movie.data_source.local.dto.getRequestOfAllMovies
import com.cairosquad.repository.movie.data_source.local.dto.getRequestOfFreeToWatchMovies
import com.cairosquad.repository.movie.data_source.local.dto.getRequestOfMoreRecommendedMovies
import com.cairosquad.repository.movie.data_source.local.dto.getRequestOfMovie
import com.cairosquad.repository.movie.data_source.local.dto.getRequestOfMoviesByCategory
import com.cairosquad.repository.movie.data_source.local.dto.getRequestOfNowPlayingMovies
import com.cairosquad.repository.movie.data_source.local.dto.getRequestOfPersonalizedMovies
import com.cairosquad.repository.movie.data_source.local.dto.getRequestOfPopularMovies
import com.cairosquad.repository.movie.data_source.local.dto.getRequestOfSimilarMovies
import com.cairosquad.repository.movie.data_source.local.dto.getRequestOfSuggestedMovies
import com.cairosquad.repository.movie.data_source.local.dto.getRequestOfTopRatedMovies
import com.cairosquad.repository.movie.data_source.local.dto.getRequestOfTrendingMovies
import com.cairosquad.repository.movie.data_source.local.dto.getRequestOfUpcomingMovies
import com.cairosquad.repository.movie.data_source.local.dto.toCacheDtoList
import com.cairosquad.repository.movie.data_source.local.dto.toEntityList
import com.cairosquad.repository.movie.data_source.local.dto.toRequestWithMoviesCacheDto
import com.cairosquad.repository.movie.data_source.remote.RemoteMovieDataSource
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.toEntity
import com.cairosquad.repository.utils.mappers.tryToCall
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfMovieReviews
import com.cairosquad.repository.utils.sharedDto.local.toEntityList
import com.cairosquad.repository.utils.sharedDto.local.toRequestWithReviewsCacheDto
import java.util.Date

class MovieRepositoryImpl(
    private val remoteMovieDataSource: RemoteMovieDataSource,
    private val moviesCacheDataSource: MoviesCacheDataSource
) : MoviesRepository {

    override suspend fun getSimilarMovies(movieId: Long, page: Int): List<Movie> {
        return getMovies(
            remoteFetcher = { remoteMovieDataSource.getSimilarMovies(movieId, page) },
            requestCache = getRequestOfSimilarMovies(movieId, page)
        )
    }

    override suspend fun getPersonalizedMovies(page: Int): List<Movie> {
        return getMovies(
            remoteFetcher = { remoteMovieDataSource.getPersonalizedMovies(page) },
            requestCache = getRequestOfPersonalizedMovies(page)
        )
    }

    override suspend fun getSuggestedMovies(): List<Movie> {
        return getMovies(
            remoteFetcher = { remoteMovieDataSource.getSuggestedMovies() },
            requestCache = getRequestOfSuggestedMovies()
        )
    }

    override suspend fun getTopRatingMovies(page: Int, genreId: String?): List<Movie> {
        return getMovies(
            remoteFetcher = { remoteMovieDataSource.getTopRatingMovies(page,genreId) },
            requestCache = getRequestOfTopRatedMovies(page, genreId)
        )
    }

    override suspend fun getUpcomingMovies(page: Int, categoryId: String?): List<Movie> {
        return getMovies(
            remoteFetcher = { remoteMovieDataSource.getUpcomingMovies(page, categoryId) },
            requestCache = getRequestOfUpcomingMovies(page, categoryId)
        )
    }

    override suspend fun getNowPlayingMovies(page: Int, categoryId: String?): List<Movie> {
        return getMovies(
            remoteFetcher = {remoteMovieDataSource.getNowPlayingMovies(page, categoryId) },
            requestCache = getRequestOfNowPlayingMovies(page, categoryId)
        )
    }

    override suspend fun getTrendingMovies(page: Int, categoryId: String?): List<Movie> {
        return getMovies(
            remoteFetcher = { remoteMovieDataSource.getTrendingMovies(page, categoryId) },
            requestCache = getRequestOfTrendingMovies(page, categoryId)
        )
    }

    override suspend fun getMoreRecommendedMovies(page: Int, categoryId: String?): List<Movie> {
        return getMovies(
            remoteFetcher = { remoteMovieDataSource.getMoreRecommendedMovies(page, categoryId) },
            requestCache = getRequestOfMoreRecommendedMovies(page, categoryId)
        )
    }

    override suspend fun getFreeToWatchMovies(page: Int, categoryId: String?): List<Movie> {
        return getMovies(
            remoteFetcher = { remoteMovieDataSource.getFreeToWatchMovies(page, categoryId) },
            requestCache = getRequestOfFreeToWatchMovies(page, categoryId)
        )
    }

    override suspend fun getMoviesByCategory(page: Int, categoryId: String): List<Movie> {
        return getMovies(
            remoteFetcher = { remoteMovieDataSource.getMoviesByCategory(categoryId, page) },
            requestCache = getRequestOfMoviesByCategory(page, categoryId)
        )
    }

    override suspend fun getPopularMovies(page: Int, categoryId: String?): List<Movie> {
        return getMovies(
            remoteFetcher = { remoteMovieDataSource.getPopularMovies(page, categoryId) },
            requestCache = getRequestOfPopularMovies(page, categoryId)
        )
    }

    override suspend fun getAllMovies(page: Int, categoryId: String?, sortType: SortType?): List<Movie> {
        return getMovies(
            remoteFetcher = { remoteMovieDataSource.getAllMovies(page, categoryId, sortType?.sortBy) },
            requestCache = getRequestOfAllMovies(page, categoryId, sortType)
        )
    }

    private suspend fun getMovies(
        remoteFetcher: suspend () -> List<MovieRemoteDto>,
        requestCache: String
    ):List<Movie> {
        moviesCacheDataSource.deleteExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
        return moviesCacheDataSource
            .getMoviesByRequest(request = requestCache)
            .toEntityList()
            .takeIf { it.isNotEmpty() }
            ?: tryToCall {
                val genres = remoteMovieDataSource.getMoviesGenres().map { it.toEntity() }
                remoteFetcher()
                    .map { it.toEntity(genres) }
                    .also { movies ->
                        moviesCacheDataSource.cacheRequestWithMovies(
                            movies.toRequestWithMoviesCacheDto(
                                request = requestCache
                            )
                        )
                    }
            }
    }

    override suspend fun getMovieById(movieId: Long): Movie {
        moviesCacheDataSource.deleteExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
        return moviesCacheDataSource
            .getMoviesByRequest(request = getRequestOfMovie(movieId))
            .toEntityList()
            .firstOrNull()
            ?: tryToCall {
                remoteMovieDataSource.getMovieById(movieId).toEntity(
                    remoteMovieDataSource.getVideoKey(movieId)
                )
            }.also { movie ->
                moviesCacheDataSource.cacheRequestWithMovies(
                    listOf(movie).toRequestWithMoviesCacheDto(request = "movie/${movieId}")
                )
            }
    }

    override suspend fun getMovieReviews(movieId: Long, page: Int): List<Review> {
        return moviesCacheDataSource
            .getMovieReviewsByRequest(getRequestOfMovieReviews(page, movieId))
            .toEntityList()
            .takeIf { it.isNotEmpty() }
            ?:tryToCall {
                remoteMovieDataSource.getMovieReviews(movieId, page).map { it.toEntity() }
            }.also {
                moviesCacheDataSource.cacheRequestWithReviews(
                    it.toRequestWithReviewsCacheDto(
                        getRequestOfMovieReviews(page, movieId)
                    )
                )
            }
    }

    override suspend fun getMovieTopCast(movieId: Long, page: Int): List<Artist> {
        return tryToCall {
            remoteMovieDataSource.getMovieTopCast(movieId, page).map { it.toEntity() }
        }
    }

    override suspend fun getMoviesGenres(): List<Genre> {
        return moviesCacheDataSource
            .getMovieGenres()
            .toEntityList()
            .takeIf { it.isNotEmpty() }
            ?: tryToCall {
                remoteMovieDataSource.getMoviesGenres()
                    .map { it.toEntity() }
                    .also {
                        moviesCacheDataSource.cacheMovieGenres(it.toCacheDtoList())
                    }
            }
    }

    private companion object {
        private const val CACHE_EXPIRATION_MILLIS = 3_600_000
    }
}