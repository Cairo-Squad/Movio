package com.cairosquad.local.cache.movie

import com.cairosquad.local.cache.genre.GenreDao
import com.cairosquad.local.cache.request.RequestCachedDao
import com.cairosquad.local.cache.reviews.ReviewDao
import com.cairosquad.repository.movie.data_source.local.MoviesLocalDataSource
import com.cairosquad.repository.movie.data_source.local.dto.GenreOfMovieCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.MovieGenreCacheCrossRef
import com.cairosquad.repository.movie.data_source.local.dto.RequestMovieCacheCrossRef
import com.cairosquad.repository.movie.data_source.local.dto.RequestWithMoviesCacheDto
import com.cairosquad.repository.utils.sharedDto.local.RequestReviewCacheCrossRef
import com.cairosquad.repository.utils.sharedDto.local.RequestWithReviewsCacheDto
import com.cairosquad.repository.utils.sharedDto.local.ReviewCacheDto

class MoviesLocalDataSourceImpl(
    private val moviesCacheDao: MoviesCacheDao,
    private val requestCachedDao: RequestCachedDao,
    private val genreDao: GenreDao,
    private val reviewDao: ReviewDao
): MoviesLocalDataSource {
    override suspend fun insertRequestWithMovies(requestWithMovies: RequestWithMoviesCacheDto) {
        moviesCacheDao.insertMoviesWithoutGenre(requestWithMovies.movies.map { it.movieWithoutGenre })
        genreDao.insertMovieGenres(genres = requestWithMovies.movies.map { it.genres }.flatten().toSet().toList())
        moviesCacheDao.insertMovieGenreCacheCrossRef(
            requestWithMovies.movies.map { movie ->
                movie.genres.map { genre ->
                    MovieGenreCacheCrossRef(movie.movieWithoutGenre.id, genre.id)
                }
            }.flatten()
        )
        requestCachedDao.insertRequestCache(requestWithMovies.request)
        moviesCacheDao.insertRequestMovieCacheCrossRef(
            RequestMovieCacheCrossRef.fromRequestAndMovieList(
                request = requestWithMovies.request,
                movies = requestWithMovies.movies
            )
        )
    }

    override suspend fun getMoviesByRequest(request: String): List<MovieCacheDto> {

        return moviesCacheDao.getMoviesByRequest(request)?.movies ?: emptyList()
    }

    override suspend fun insertMovieGenres(genres: List<GenreOfMovieCacheDto>) {
        genreDao.insertMovieGenres(genres)
    }

    override suspend fun getMovieGenres(): List<GenreOfMovieCacheDto> {
        return genreDao.getMovieGenres()
    }

    override suspend fun getMovieReviewsByRequest(request: String): List<ReviewCacheDto> {
        return reviewDao.getReviewsByRequest(request)?.reviews ?: emptyList()
    }

    override suspend fun insertRequestWithReviews(requestWithReviewsCacheDto: RequestWithReviewsCacheDto) {
        requestCachedDao.insertRequestCache(requestWithReviewsCacheDto.request)
        reviewDao.insertReviews(requestWithReviewsCacheDto.reviews)
        reviewDao.insertRequestReviewCacheCrossRef(
            RequestReviewCacheCrossRef.fromRequestAndReviewList(
                request = requestWithReviewsCacheDto.request,
                reviews = requestWithReviewsCacheDto.reviews
            )
        )
    }

    override suspend fun deleteExpiredCache(timestamp: Long) {
        requestCachedDao.deleteExpiredRequestCache(timestamp)

        moviesCacheDao.deleteExpiredMovieWithoutGenreCache(timestamp)
        moviesCacheDao.deleteUnwantedRequestMovieCacheCrossRef()

        genreDao.deleteExpiredMovieGenreCache(timestamp)
        moviesCacheDao.deleteUnwantedMovieGenreCacheCrossRef()

        reviewDao.deleteExpiredReviewCache(timestamp)
        reviewDao.deleteUnwantedRequestReviewCacheCrossRef()
    }

}