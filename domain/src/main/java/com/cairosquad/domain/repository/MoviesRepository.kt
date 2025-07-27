package com.cairosquad.domain.repository

import com.cairosquad.domain.model.SortType
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review

interface MoviesRepository {
    suspend fun getSimilarMovies(movieId: Long, page: Int): List<Movie>

    suspend fun getPersonalizedMovies(page: Int): List<Movie>

    suspend fun getSuggestedMovies(): List<Movie>

    suspend fun getTopRatingMovies(page: Int, genreId: Long?): List<Movie>

    suspend fun getUpcomingMovies(page: Int, genreId: Long?): List<Movie>

    suspend fun getNowPlayingMovies(page: Int, genreId: Long?): List<Movie>

    suspend fun getTrendingMovies(page: Int, genreId: Long?): List<Movie>

    suspend fun getMoreRecommendedMovies(page: Int, genreId: Long?): List<Movie>

    suspend fun getFreeToWatchMovies(page: Int, genreId: Long?): List<Movie>

    suspend fun getMoviesByCategory(page: Int, genreId: Long): List<Movie>

    suspend fun getPopularMovies(page: Int, genreId: Long?): List<Movie>

    suspend fun getAllMovies(page: Int, genreId: Long?, sortType: SortType?): List<Movie>

    suspend fun getMoviesByQuery(query: String, page: Int): List<Movie>

    suspend fun getMovieById(id: Long): Movie

    suspend fun getMovieReviews(movieId: Long, page: Int): List<Review>

    suspend fun getMoviesOfArtist(artistId: Long): List<Movie>

    suspend fun getMoviesGenres(): List<Genre>
}