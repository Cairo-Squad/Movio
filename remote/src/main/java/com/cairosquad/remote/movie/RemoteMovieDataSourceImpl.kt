package com.cairosquad.remote.movie

import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.movie.data_source.remote.RemoteMovieDataSource
import com.cairosquad.repository.movie.data_source.remote.dto.MovieDetailsRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto

class RemoteMovieDataSourceImpl(
    private val apiService: MovieApiService,
) : RemoteMovieDataSource {

    override suspend fun getMovie(movieId: Long): MovieDetailsRemoteDto {
        return safeCallApi { apiService.getMovie(movieId) }
    }

    override suspend fun getMovieReviews(movieId: Long, page: Int): List<ReviewRemoteDto> {
        return safeCallApi { apiService.getMovieReviews(movieId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSimilarMovies(movieId: Long, page: Int): List<MovieRemoteDto> {
        return safeCallApi { apiService.getSimilarMovies(movieId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMovieTopCast(movieId: Long, page: Int): List<ArtistRemoteDto> {
        return safeCallApi { apiService.getMovieTopCast(movieId, page) }
            .cast?.filterNotNull().orEmpty()
    }

    override suspend fun getVideoKey(movieId: Long): String {
        return safeCallApi { apiService.getVideoKey(movieId).getVideoKey() ?: "" }
    }
}