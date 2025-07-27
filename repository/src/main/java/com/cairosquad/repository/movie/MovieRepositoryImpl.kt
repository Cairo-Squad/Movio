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
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfAllMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfFreeToWatchMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfMoreRecommendedMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfMovie
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfMovieReviews
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfMoviesByCategory
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfNowPlayingMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfPersonalizedMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfPopularMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSearchedMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSimilarMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfSuggestedMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfTopRatedMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfTrendingMovies
import com.cairosquad.repository.utils.sharedDto.local.getCacheCodeOfUpcomingMovies
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
            cacheCode = getCacheCodeOfSimilarMovies(movieId, page)
        )
    }

    override suspend fun getPersonalizedMovies(page: Int): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getPersonalizedMovies(page) },
            cacheCode = getCacheCodeOfPersonalizedMovies(page)
        )
    }

    override suspend fun getSuggestedMovies(): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getSuggestedMovies() },
            cacheCode = getCacheCodeOfSuggestedMovies()
        )
    }

    override suspend fun getTopRatingMovies(page: Int, genreId: Long?): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getTopRatingMovies(page, genreId) },
            cacheCode = getCacheCodeOfTopRatedMovies(page, genreId)
        )
    }

    override suspend fun getUpcomingMovies(page: Int, genreId: Long?): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getUpcomingMovies(page, genreId) },
            cacheCode = getCacheCodeOfUpcomingMovies(page, genreId)
        )
    }

    override suspend fun getNowPlayingMovies(page: Int, genreId: Long?): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getNowPlayingMovies(page, genreId) },
            cacheCode = getCacheCodeOfNowPlayingMovies(page, genreId)
        )
    }

    override suspend fun getTrendingMovies(page: Int, genreId: Long?): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getTrendingMovies(page, genreId) },
            cacheCode = getCacheCodeOfTrendingMovies(page, genreId)
        )
    }

    override suspend fun getMoreRecommendedMovies(page: Int, genreId: Long?): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getMoreRecommendedMovies(page, genreId) },
            cacheCode = getCacheCodeOfMoreRecommendedMovies(page, genreId)
        )
    }

    override suspend fun getFreeToWatchMovies(page: Int, genreId: Long?): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getFreeToWatchMovies(page, genreId) },
            cacheCode = getCacheCodeOfFreeToWatchMovies(page, genreId)
        )
    }

    override suspend fun getMoviesByCategory(page: Int, genreId: Long): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getMoviesByCategory(genreId, page) },
            cacheCode = getCacheCodeOfMoviesByCategory(page, genreId)
        )
    }

    override suspend fun getPopularMovies(page: Int, genreId: Long?): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getPopularMovies(page, genreId) },
            cacheCode = getCacheCodeOfPopularMovies(page, genreId)
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
            cacheCode = getCacheCodeOfAllMovies(page, genreId, sortType)
        )
    }

    override suspend fun getMoviesByQuery(query: String, page: Int): List<Movie> {
        return getMovies(
            remoteFetcher = { moviesRemoteDataSource.getMoviesByQuery(query, page) },
            cacheCode = getCacheCodeOfSearchedMovies(query, page)
        )
    }

    private suspend fun getMovies(
        remoteFetcher: suspend () -> List<MovieRemoteDto>,
        cacheCode: String
    ): List<Movie> {
        moviesLocalDataSource.deleteExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
        return moviesLocalDataSource.getMoviesByCacheCode(cacheCode = cacheCode)
            .toEntityList()
            .takeIf { it.isNotEmpty() }
            ?: tryToCall {
                val genres = moviesRemoteDataSource.getMoviesGenres().map { it.toEntity() }
                remoteFetcher()
                    .map { it.toEntity(genres) }
                    .also { movies ->
                        moviesLocalDataSource.insertCacheCodeWithMovies(
                            movies.toRequestWithMoviesCacheDto(
                                request = cacheCode
                            )
                        )
                    }
            }
    }

    override suspend fun getMovieById(id: Long): Movie {
        moviesLocalDataSource.deleteExpiredCache(Date().time - CACHE_EXPIRATION_MILLIS)
        return moviesLocalDataSource
            .getMoviesByCacheCode(cacheCode = getCacheCodeOfMovie(id))
            .toEntityList()
            .firstOrNull()
            ?: tryToCall {
                moviesRemoteDataSource.getMovieById(id).toEntity(
                    moviesRemoteDataSource.getVideoKey(id)
                )
            }.also { movie ->
                moviesLocalDataSource.insertCacheCodeWithMovies(
                    listOf(movie).toRequestWithMoviesCacheDto(request = getCacheCodeOfMovie(id))
                )
            }
    }

    override suspend fun getMovieReviews(movieId: Long, page: Int): List<Review> {
        return moviesLocalDataSource
            .getMovieReviewsByCacheCode(getCacheCodeOfMovieReviews(page, movieId))
            .toEntityList()
            .takeIf { it.isNotEmpty() }
            ?: tryToCall {
                moviesRemoteDataSource.getMovieReviews(movieId, page).map { it.toEntity() }
            }.also {
                moviesLocalDataSource.insertCacheCodeWithReviews(
                    it.toRequestWithReviewsCacheDto(
                        getCacheCodeOfMovieReviews(page, movieId)
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