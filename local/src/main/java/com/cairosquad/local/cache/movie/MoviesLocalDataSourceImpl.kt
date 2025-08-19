package com.cairosquad.local.cache.movie

import com.cairosquad.local.cache.cacheCode.CacheCodeDao
import com.cairosquad.local.cache.genre.GenreDao
import com.cairosquad.local.cache.reviews.ReviewDao
import com.cairosquad.repository.movie.data_source.local.MoviesLocalDataSource
import com.cairosquad.repository.movie.data_source.local.dto.CacheCodeMovieCrossRef
import com.cairosquad.repository.movie.data_source.local.dto.CacheCodeWithMoviesCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.GenreOfMovieCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.MovieCacheDto
import com.cairosquad.repository.movie.data_source.local.dto.MovieGenreCacheCrossRef
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeReviewCacheCrossRef
import com.cairosquad.repository.utils.sharedDto.local.CacheCodeWithReviewsCacheDto
import com.cairosquad.repository.utils.sharedDto.local.ReviewCacheDto
import javax.inject.Inject

class MoviesLocalDataSourceImpl @Inject constructor(
    private val moviesCacheDao: MoviesCacheDao,
    private val cacheCodeDao: CacheCodeDao,
    private val genreDao: GenreDao,
    private val reviewDao: ReviewDao
) : MoviesLocalDataSource {
    override suspend fun insertCacheCodeWithMovies(cacheCodeWithMovies: CacheCodeWithMoviesCacheDto) {
        moviesCacheDao
            .insertMoviesWithoutGenre(
                cacheCodeWithMovies.movies
                    .map { it.movieWithoutGenre }
            )

        genreDao.insertMovieGenres(
            genres = cacheCodeWithMovies.movies
                .map { it.genres }
                .flatten()
                .distinctBy { it.genreIdWithLanguage }
        )
        moviesCacheDao.insertCrossRefForMovieAndGenreCache(
            cacheCodeWithMovies.movies
                .map { movie ->
                    MovieGenreCacheCrossRef.fromMovie(movie)
                }.flatten()
        )
        cacheCodeDao.insertCacheCode(
            cacheCodeWithMovies.cacheCode
        )
        moviesCacheDao.insertCrossRefForCacheCodeAndMovieCache(
            CacheCodeMovieCrossRef.fromCacheCodeAndMovieList(
                cacheCode = cacheCodeWithMovies.cacheCode,
                movies = cacheCodeWithMovies.movies
            )
        )
    }

    override suspend fun getMoviesByCacheCode(cacheCode: String): List<MovieCacheDto> {
        return moviesCacheDao.getMoviesByCacheCode(cacheCode)?.movies ?: emptyList()
    }

    override suspend fun insertMovieGenres(genres: List<GenreOfMovieCacheDto>) {
        genreDao.insertMovieGenres(genres)
    }

    override suspend fun getMovieGenres(): List<GenreOfMovieCacheDto> {
        return genreDao.getMovieGenres()
    }

    override suspend fun getMovieGenresByLanguage(language: String): List<GenreOfMovieCacheDto> {
        return genreDao.getMovieGenresByLanguage(language = language)
    }

    override suspend fun getMovieReviewsByCacheCode(cacheCode: String): List<ReviewCacheDto> {
        return reviewDao.getReviewsByCacheCode(cacheCode)?.reviews ?: emptyList()
    }

    override suspend fun insertCacheCodeWithReviews(cacheCodeWithReviewsCacheDto: CacheCodeWithReviewsCacheDto) {
        cacheCodeDao.insertCacheCode(cacheCodeWithReviewsCacheDto.cacheCode)
        reviewDao.insertReviews(cacheCodeWithReviewsCacheDto.reviews)
        reviewDao.insertRequestReviewCacheCrossRef(
            CacheCodeReviewCacheCrossRef.fromRequestAndReviewList(
                cacheCode = cacheCodeWithReviewsCacheDto.cacheCode,
                reviews = cacheCodeWithReviewsCacheDto.reviews
            )
        )
    }

    override suspend fun deleteExpiredCache(timestamp: Long) {
        cacheCodeDao.deleteExpiredCacheCode(timestamp)

        moviesCacheDao.deleteExpiredMovieWithoutGenreCache(timestamp)
        moviesCacheDao.deleteCrossRefForNonExistingCacheCodeAndMovieCache()

        genreDao.deleteExpiredGenreOfMovieCache(timestamp)
        moviesCacheDao.deleteCrossRefForNonExistingMovieAndGenreCache()

        reviewDao.deleteExpiredReviewCache(timestamp)
        reviewDao.deleteCrossRefForNonexistingRequestReviewCache()
    }

}