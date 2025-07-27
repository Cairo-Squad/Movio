package com.cairosquad.remote.movie


import com.cairosquad.repository.movie.data_source.remote.dto.CreditResponse
import com.cairosquad.repository.movie.data_source.remote.dto.MovieDetailsRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.VideoResponse
import com.cairosquad.repository.utils.sharedDto.remote.GenreResponse
import com.cairosquad.repository.utils.sharedDto.remote.ResultResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/{movieId}")
    suspend fun getMovieById(
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

    @GET("movie/{movieId}/videos")
    suspend fun getVideoKey(
        @Path("movieId") movieId: Long
    ): VideoResponse

    @GET("discover/movie?sort_by=vote_average.desc&vote_count.gte=200&include_adult=false&include_video=false")
    suspend fun getTopRatingMovies(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String?,
    ): ResultResponse<MovieRemoteDto>

    @GET("discover/movie?sort_by=popularity.desc&include_adult=false&include_video=false&with_release_type=2|3")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String?,
        @Query("release_date.gte") minDate: String?,
        @Query("release_date.lte") maxDate: String?,
    ): ResultResponse<MovieRemoteDto>

    @GET("discover/movie?sort_by=popularity.desc&include_adult=false&include_video=false&with_release_type=2|3")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String?,
        @Query("release_date.gte") minDate: String?,
        @Query("release_date.lte") maxDate: String?,
    ): ResultResponse<MovieRemoteDto>

    @GET("discover/movie?sort_by=popularity.desc&include_adult=false&include_video=false&vote_count.gte=50")
    suspend fun getTrendingMovies(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String?,
        @Query("release_date.lte") maxDate: String?,
        @Query("release_date.gte") minDate: String?,
    ): ResultResponse<MovieRemoteDto>


    @GET("discover/movie?sort_by=vote_count.desc")
    suspend fun getMoreRecommendedMovies(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String?,
    ): ResultResponse<MovieRemoteDto>

    @GET("discover/movie?with_watch_providers=free")
    suspend fun getFreeToWatchMovies(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String?,
    ): ResultResponse<MovieRemoteDto>

    @GET("discover/movie")
    suspend fun getMoviesByCategory(
        @Query("with_genres") categoryId: String,
        @Query("page") page: Int,
    ): ResultResponse<MovieRemoteDto>

    @GET("search/movie")
    suspend fun getMoviesByQuery(
        @Query("query") query: String,
        @Query("page") page: Int
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

    @GET("movie/top_rated")
    suspend fun getPersonalizedMovies(
        @Query("page") page: Int
    ): ResultResponse<MovieRemoteDto>

    @GET("movie/now_playing")
    suspend fun getSuggestedMovies(): ResultResponse<MovieRemoteDto>
}