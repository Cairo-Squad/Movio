package com.cairosquad.repository.movie.data_source.remote

import com.cairosquad.repository.movie.data_source.remote.dto.MovieDetailsRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto


interface RemoteMovieDataSource {
    suspend fun getMovie(movieId: Long): MovieDetailsRemoteDto

    suspend fun getMovieReviews(movieId: Long, page: Int): List<ReviewRemoteDto>

    suspend fun getSimilarMovies(movieId: Long, page: Int): List<MovieRemoteDto>

    suspend fun getMovieTopCast(movieId: Long, page: Int): List<ArtistRemoteDto>

    suspend fun getTopRatingMovies(page: Int): List<MovieRemoteDto>

    suspend fun getUpcomingMovies(page: Int): List<MovieRemoteDto>

    suspend fun getNowPlayingMovies(page: Int): List<MovieRemoteDto>

    suspend fun getTrendingMovies(page: Int): List<MovieRemoteDto>

    suspend fun getMoreRecommendedMovies(page: Int): List<MovieRemoteDto>

    suspend fun getFreeToWatchMovies(page: Int): List<MovieRemoteDto>

    suspend fun getMoviesByCategory(categoryId: String, page: Int): List<MovieRemoteDto>
}