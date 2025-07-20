package com.cairosquad.remote.movie

import com.cairosquad.repository.movie.data_source.remote.RemoteMovieDataSource
import com.cairosquad.repository.movie.data_source.remote.dto.MovieDetailsRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto

class RemoteMovieDataSourceImpl(
    private val apiService: MovieApiService,
) : RemoteMovieDataSource {

    override suspend fun getMovie(movieId: Long): MovieDetailsRemoteDto {
        return apiService.getMovie(movieId)
    }

    override suspend fun getMovieReviews(movieId: Long, page: Int): List<ReviewRemoteDto> {
        return apiService.getMovieReviews(movieId, page)
    }

    override suspend fun getSimilarMovies(movieId: Long, page: Int): List<MovieRemoteDto> {
        return apiService.getSimilarMovies(movieId, page)
    }

    override suspend fun getMovieTopCast(movieId: Long, page: Int): List<ArtistRemoteDto> {
        return apiService.getMovieTopCast(movieId, page).cast?.filterNotNull().orEmpty()
    }
}