package com.cairosquad.remote.movie


import com.cairosquad.repository.movie.data_source.remote.dto.CreditResponse
import com.cairosquad.repository.movie.data_source.remote.dto.MovieDetailsRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ResultResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/{movieId}")
    suspend fun getMovie(
        @Path("movieId") movieId: Long,
        @Query("api_key") apiKey: String
    ): MovieDetailsRemoteDto

    @GET("movie/{movieId}/reviews")
    suspend fun getMovieReviews(
        @Path("movieId") movieId: Long,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): ResultResponse<ReviewRemoteDto>

    @GET("movie/{movieId}/similar")
    suspend fun getSimilarMovies(
        @Path("movieId") movieId: Long,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): ResultResponse<MovieRemoteDto>

    @GET("movie/{movieId}/credits")
    suspend fun getMovieTopCast(
        @Path("movieId") movieId: Long,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): CreditResponse
}