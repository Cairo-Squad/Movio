package com.cairosquad.local.cache.movie

import com.cairosquad.local.cache.genre.GenreDao
import com.cairosquad.local.cache.request.RequestCachedDao
import com.cairosquad.repository.movie.data_source.local.MoviesCacheDataSource
import com.cairosquad.repository.movie.data_source.local.dto.MovieCacheDtoNew
import com.cairosquad.repository.movie.data_source.local.dto.MovieGenreCacheCrossRef
import com.cairosquad.repository.movie.data_source.local.dto.RequestMovieCacheCrossRef
import com.cairosquad.repository.movie.data_source.local.dto.RequestWithMoviesCacheDto

class MoviesCacheDataSourceImpl(
    private val moviesCacheDao: MoviesCacheDao,
    private val requestCachedDao: RequestCachedDao,
    private val genreDao: GenreDao
): MoviesCacheDataSource {
    override suspend fun cacheRequestWithMovies(requestWithMovies: RequestWithMoviesCacheDto) {
        moviesCacheDao.insertMoviesWithoutGenre(requestWithMovies.movies.map { it.movieWithoutGenre })
        genreDao.insertGenres(genres = requestWithMovies.movies.map { it.genres }.flatten().toSet().toList())
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

    override suspend fun getMoviesByRequest(request: String): List<MovieCacheDtoNew> {
        return moviesCacheDao.getMoviesByRequest(request)?.movies ?: emptyList()

    }

    override suspend fun deleteExpiredCache(timestamp: Long) {
        moviesCacheDao.deleteExpiredMovieWithoutGenreCache(timestamp)
        requestCachedDao.deleteExpiredRequestCache(timestamp)
        moviesCacheDao.deleteUnwantedRequestMovieCacheCrossRef()
        genreDao.deleteExpiredGenreCache(timestamp)
        moviesCacheDao.deleteUnwantedMovieGenreCacheCrossRef()
    }

}