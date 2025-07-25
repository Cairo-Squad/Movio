package com.cairosquad.domain.repository

import com.cairosquad.domain.model.SortType
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review

interface MoviesRepository {
    suspend fun getMovie(movieId: Long): Movie

    suspend fun getMovieReviews(movieId: Long, page: Int): List<Review>

    suspend fun getSimilarMovies(movieId: Long, page: Int): List<Movie>

    suspend fun getMovieTopCast(movieId: Long, page: Int): List<Artist>

    suspend fun getPersonalizedMovies(page: Int): List<Movie>

    suspend fun getSuggestedMovies(): List<Movie>

    suspend fun getTopRatingMovies(page: Int, genreId: String?): List<Movie>

    suspend fun getUpcomingMovies(page: Int,categoryId: String?): List<Movie>

    suspend fun getNowPlayingMovies(page: Int,categoryId: String?): List<Movie>

    suspend fun getTrendingMovies(page: Int,categoryId: String?): List<Movie>

    suspend fun getMoreRecommendedMovies(page: Int,categoryId: String?): List<Movie>

    suspend fun getFreeToWatchMovies(page: Int,categoryId: String?): List<Movie>

    suspend fun getMoviesByCategory( page: Int,categoryId: String,): List<Movie>

    suspend fun getMoviesGenres(): List<Genre>

    suspend fun getPopularMovies(page: Int,categoryId: String?): List<Movie>

    suspend fun getAllMovies(page:Int,categoryId: String?,sortType: SortType?) : List<Movie>
}