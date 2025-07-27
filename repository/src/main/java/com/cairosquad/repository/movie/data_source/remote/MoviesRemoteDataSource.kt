package com.cairosquad.repository.movie.data_source.remote

import com.cairosquad.repository.artists.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.GenreDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieDetailsRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto


interface MoviesRemoteDataSource {
    suspend fun getMovieById(movieId: Long): MovieDetailsRemoteDto

    suspend fun getMovieReviews(movieId: Long, page: Int): List<ReviewRemoteDto>

    suspend fun getSimilarMovies(movieId: Long, page: Int): List<MovieRemoteDto>

    suspend fun getMovieTopCast(movieId: Long, page: Int): List<ArtistRemoteDto>

    suspend fun getVideoKey(movieId: Long): String

    suspend fun getTopRatingMovies(page: Int,categoryId: String?): List<MovieRemoteDto>

    suspend fun getUpcomingMovies(page: Int,categoryId: String?): List<MovieRemoteDto>

    suspend fun getNowPlayingMovies(page: Int,categoryId: String?): List<MovieRemoteDto>

    suspend fun getTrendingMovies(page: Int,categoryId: String?): List<MovieRemoteDto>

    suspend fun getMoreRecommendedMovies(page: Int,categoryId: String?): List<MovieRemoteDto>

    suspend fun getFreeToWatchMovies(page: Int,categoryId: String?): List<MovieRemoteDto>

    suspend fun getMoviesByCategory(categoryId: String, page: Int): List<MovieRemoteDto>

    suspend fun getMoviesByQuery(query: String, page: Int): List<MovieRemoteDto>

    suspend fun getMoviesGenres(): List<GenreDto>

    suspend fun getPopularMovies(page: Int,categoryId: String?): List<MovieRemoteDto>

    suspend fun getAllMovies(page: Int,categoryId: String?,sortBy: String?) : List<MovieRemoteDto>

    suspend fun getPersonalizedMovies(page : Int): List<MovieRemoteDto>

    suspend fun getSuggestedMovies(): List<MovieRemoteDto>
}