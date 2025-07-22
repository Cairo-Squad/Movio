package com.cairosquad.remote.movie


import com.cairosquad.repository.movie.data_source.remote.dto.CreditResponse
import com.cairosquad.repository.movie.data_source.remote.dto.MovieDetailsRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.GenreResponse
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ResultResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/{movieId}")
    suspend fun getMovie(
        @Path("movieId") movieId: Long
    ): MovieDetailsRemoteDto

    @GET("movie/{movieId}/reviews")
    suspend fun getMovieReviews(
        @Path("movieId") movieId: Long,
        @Query("page") page: Int
    ): ResultResponse<ReviewRemoteDto>

    @GET("movie/{movieId}/similar")
    suspend fun getSimilarMovies(
        @Path("movieId") movieId: Long,
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String? = null
    ): ResultResponse<MovieRemoteDto>

    @GET("movie/{movieId}/credits")
    suspend fun getMovieTopCast(
        @Path("movieId") movieId: Long,
        @Query("page") page: Int
    ): CreditResponse

    @GET("movie/top_rated")
    suspend fun getTopRatingMovies(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String? = null
    ): ResultResponse<MovieRemoteDto>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String? = null
    ): ResultResponse<MovieRemoteDto>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String? = null
    ): ResultResponse<MovieRemoteDto>

    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String? = null
    ): ResultResponse<MovieRemoteDto>


    @GET("discover/movie")
    suspend fun getMoreRecommendedMovies(
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String = "vote_count.desc",
        @Query("with_genres") withGenres: String? = null,
    ): ResultResponse<MovieRemoteDto>

    @GET("discover/movie")
    suspend fun getFreeToWatchMovies(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String? = null,
        @Query("with_watch_providers") free: String = "free" // TODO: find better way
    ): ResultResponse<MovieRemoteDto>

    @GET("discover/movie")
    suspend fun getMoviesByCategory(
        @Query("with_genres") categoryId: String,
        @Query("page") page: Int,
    ): ResultResponse<MovieRemoteDto>

    @GET("genre/movie/list")
    suspend fun getMoviesGenres(
    ): GenreResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String? = null,
    ): ResultResponse<MovieRemoteDto>


    @GET("discover/movie")
    suspend fun getAllMovies(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String? = null,
        @Query("sort_by") sortBy: String? = null
    ): ResultResponse<MovieRemoteDto>
}

