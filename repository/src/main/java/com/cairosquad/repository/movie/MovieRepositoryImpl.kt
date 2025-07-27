package com.cairosquad.repository.movie

import com.cairosquad.domain.model.SortType
import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review
import com.cairosquad.repository.artists.data_source.remote.toEntity
import com.cairosquad.repository.movie.data_source.local.MoviesLocalDataSource
import com.cairosquad.repository.movie.data_source.local.toCacheDtoList
import com.cairosquad.repository.movie.data_source.local.toEntityList
import com.cairosquad.repository.movie.data_source.local.toRequestWithMoviesCacheDto
import com.cairosquad.repository.movie.data_source.remote.MoviesRemoteDataSource
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.movie.data_source.remote.toEntity
import com.cairosquad.repository.utils.mappers.tryToCall
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfAllMovies
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfFreeToWatchMovies
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfMoreRecommendedMovies
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfMovie
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfMovieReviews
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfMoviesByCategory
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfNowPlayingMovies
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfPersonalizedMovies
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfPopularMovies
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfSearchedMovies
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfSimilarMovies
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfSuggestedMovies
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfTopRatedMovies
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfTrendingMovies
import com.cairosquad.repository.utils.sharedDto.local.getRequestOfUpcomingMovies
import com.cairosquad.repository.utils.sharedDto.local.toEntityList
import com.cairosquad.repository.utils.sharedDto.local.toRequestWithReviewsCacheDto
import java.util.Date

class MovieRepositoryImpl(
    private val moviesRemoteDataSource: MoviesRemoteDataSource,
    private val moviesLocalDataSource: MoviesLocalDataSource
) : MoviesRepository {

    override suspend fun getSimilarMovies(movieId: Long, page: Int): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getSimilarMovies(movieId, page) },
            requestCache = getRequestOfSimilarMovies(movieId, page)
        )
    }

    override suspend fun getPersonalizedMovies(page: Int): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getPersonalizedMovies(page) },
            requestCache = getRequestOfPersonalizedMovies(page)
        )
    }

    override suspend fun getSuggestedMovies(): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getSuggestedMovies() },
            requestCache = getRequestOfSuggestedMovies()
        )
    }

    override suspend fun getTopRatingMovies(page: Int, genreId: Long?): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getTopRatingMovies(page, genreId) },
            requestCache = getRequestOfTopRatedMovies(page, genreId)
        )
    }

    override suspend fun getUpcomingMovies(page: Int, genreId: Long?): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getUpcomingMovies(page, genreId) },
            requestCache = getRequestOfUpcomingMovies(page, genreId)
        )
    }

    override suspend fun getNowPlayingMovies(page: Int, genreId: Long?): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getNowPlayingMovies(page, genreId) },
            requestCache = getRequestOfNowPlayingMovies(page, genreId)
        )
    }

    override suspend fun getTrendingMovies(page: Int, genreId: Long?): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getTrendingMovies(page, genreId) },
            requestCache = getRequestOfTrendingMovies(page, genreId)
        )
    }

    override suspend fun getMoreRecommendedMovies(page: Int, genreId: Long?): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getMoreRecommendedMovies(page, genreId) },
            requestCache = getRequestOfMoreRecommendedMovies(page, genreId)
        )
    }

    override suspend fun getFreeToWatchMovies(page: Int, genreId: Long?): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getFreeToWatchMovies(page, genreId) },
            requestCache = getRequestOfFreeToWatchMovies(page, genreId)
        )
    }

    override suspend fun getMoviesByCategory(page: Int, genreId: Long): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getMoviesByCategory(genreId, page) },
            requestCache = getRequestOfMoviesByCategory(page, genreId)
        )
    }

    override suspend fun getPopularMovies(page: Int, genreId: Long?): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getPopularMovies(page, genreId) },
            requestCache = getRequestOfPopularMovies(page, genreId)
        )
    }

    override suspend fun getAllMovies(
        page: Int,
        genreId: Long?,
        sortType: SortType?
    ): List<Movie> {
        return getMovies(
            remoteFetcher = {
                moviesRemoteDataSource.getAllMovies(
                    page,
                    genreId,
                    sortType?.sortBy
                )
            },
            requestCache = getRequestOfAllMovies(page, genreId, sortType)
        )
    }

    override suspend fun getMoviesByQuery(query: String, page: Int): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getMoviesByQuery(query, page) },
            requestCache = getRequestOfSearchedMovies(query, page)
        )
    }

    private suspend fun getMovies(
        remoteFetcher: suspend () -> List<MovieRemoteDto>,
        requestCache: String
    ): List<Movie> {
        moviesLocalDataSource.deleteExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
        return moviesLocalDataSource
            .getMoviesByRequest(request = requestCache)
            .toEntityList()
            .takeIf { it.isNotEmpty() }
            ?: tryToCall {
                val genres = moviesRemoteDataSource.getMoviesGenres().map { it.toEntity() }
                remoteFetcher()
                    .map { it.toEntity(genres) }
                    .also { movies ->
                        moviesLocalDataSource.insertRequestWithMovies(
                            movies.toRequestWithMoviesCacheDto(
                                request = requestCache
                            )
                        )
                    }
            }
    }

    override suspend fun getMovieById(id: Long): Movie {
        moviesLocalDataSource.deleteExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
        return moviesLocalDataSource
            .getMoviesByRequest(request = getRequestOfMovie(id))
            .toEntityList()
            .firstOrNull()
            ?: tryToCall {
                moviesRemoteDataSource.getMovieById(id).toEntity(
                    moviesRemoteDataSource.getVideoKey(id)
                )
            }.also { movie ->
                moviesLocalDataSource.insertRequestWithMovies(
                    listOf(movie).toRequestWithMoviesCacheDto(request = getRequestOfMovie(id))
                )
            }
    }

    override suspend fun getMovieReviews(movieId: Long, page: Int): List<Review> {
        return moviesLocalDataSource
            .getMovieReviewsByRequest(getRequestOfMovieReviews(page, movieId))
            .toEntityList()
            .takeIf { it.isNotEmpty() }
            ?: tryToCall {
                moviesRemoteDataSource.getMovieReviews(movieId, page).map { it.toEntity() }
            }.also {
                moviesLocalDataSource.insertRequestWithReviews(
                    it.toRequestWithReviewsCacheDto(
                        getRequestOfMovieReviews(page, movieId)
                    )
                )
            }
    }

    override suspend fun getMovieTopCast(movieId: Long, page: Int): List<Artist> {
        return tryToCall {
            moviesRemoteDataSource.getMovieTopCast(movieId, page).map { it.toEntity() }
        }
    }

    override suspend fun getMoviesGenres(): List<Genre> {
        return moviesLocalDataSource
            .getMovieGenres()
            .toEntityList()
            .takeIf { it.isNotEmpty() }
            ?: tryToCall {
                moviesRemoteDataSource.getMoviesGenres()
                    .map { it.toEntity() }
                    .also {
                        moviesLocalDataSource.insertMovieGenres(it.toCacheDtoList())
                    }
            }
    }

    private companion object {
        private const val CACHE_EXPIRATION_MILLIS = 3_600_000
    }
}