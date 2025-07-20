package com.cairosquad.remote.movie

import com.cairosquad.remote.BuildConfig
import com.cairosquad.repository.movie.data_source.remote.RemoteMovieDataSource
import com.cairosquad.repository.movie.data_source.remote.dto.MovieDetailsRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto

class RemoteMovieDataSourceImpl(
    private val apiService: MovieApiService,
) : RemoteMovieDataSource {

    private val apiKey = BuildConfig.API_KEY

    override suspend fun getMovie(movieId: Long): MovieDetailsRemoteDto {
        return apiService.getMovie(movieId, apiKey)
    }

    override suspend fun getMovieReviews(movieId: Long, page: Int): List<ReviewRemoteDto> {
        return apiService.getMovieReviews(movieId, apiKey, page).results?.filterNotNull().orEmpty()
    }

    override suspend fun getSimilarMovies(movieId: Long, page: Int): List<MovieRemoteDto> {
        return apiService.getSimilarMovies(movieId, apiKey, page).results?.filterNotNull().orEmpty()
    }

    override suspend fun getMovieTopCast(movieId: Long, page: Int): List<ArtistRemoteDto> {
        return apiService.getMovieTopCast(movieId, apiKey, page).cast?.filterNotNull().orEmpty()
    }
}