package com.cairosquad.repository.movie.data_source.remote

import com.cairosquad.repository.movie.data_source.remote.dto.GenreDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieDetailsRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.utils.sharedDto.remote.StatusResponse


interface MoviesRemoteDataSource {
    suspend fun getMovieById(id: Long): MovieDetailsRemoteDto

    suspend fun getMovieReviews(movieId: Long, page: Int): List<ReviewRemoteDto>

    suspend fun getSimilarMovies(movieId: Long, page: Int): List<MovieRemoteDto>

    suspend fun getMoviesOfArtist(artistId: Long): List<MovieRemoteDto>

    suspend fun getVideoKey(movieId: Long): String

    suspend fun getTopRatingMovies(page: Int, genreId: Long?): List<MovieRemoteDto>

    suspend fun getUpcomingMovies(page: Int, genreId: Long?): List<MovieRemoteDto>

    suspend fun getNowPlayingMovies(page: Int, genreId: Long?): List<MovieRemoteDto>

    suspend fun getTrendingMovies(page: Int, genreId: Long?): List<MovieRemoteDto>

    suspend fun getMoreRecommendedMovies(page: Int, genreId: Long?): List<MovieRemoteDto>

    suspend fun getFreeToWatchMovies(page: Int, genreId: Long?): List<MovieRemoteDto>

    suspend fun getMoviesByCategory(genreId: Long, page: Int): List<MovieRemoteDto>

    suspend fun getMoviesByQuery(query: String, page: Int): List<MovieRemoteDto>

    suspend fun getMoviesGenres(): List<GenreDto>

    suspend fun getPopularMovies(page: Int, genreId: Long?): List<MovieRemoteDto>

    suspend fun getAllMovies(page: Int, genreId: Long?, sortBy: String?): List<MovieRemoteDto>

    suspend fun getPersonalizedMovies(page: Int): List<MovieRemoteDto>

    suspend fun getSuggestedMovies(): List<MovieRemoteDto>

    suspend fun addMovieRating(movieId: Long, rating: Float): StatusResponse

    suspend fun deleteMovieRating(movieId: Long)
}