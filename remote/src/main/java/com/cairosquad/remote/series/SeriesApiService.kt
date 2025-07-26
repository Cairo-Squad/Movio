package com.cairosquad.remote.series

import com.cairosquad.repository.movie.data_source.remote.dto.CreditResponse
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.VideoResponse
import com.cairosquad.repository.search.data_source.remote.dto.GenreResponse
import com.cairosquad.repository.search.data_source.remote.dto.ResultResponse
import com.cairosquad.repository.series.data_source.remote.dto.SeasonResponse
import com.cairosquad.repository.series.data_source.remote.dto.SeriesDetailsRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SeriesApiService {

    @GET("tv/{seriesId}")
    suspend fun getSeries(
        @Path("seriesId") seriesId: Long
    ): SeriesDetailsRemoteDto

    @GET("tv/{seriesId}/reviews")
    suspend fun getSeriesReviews(
        @Path("seriesId") seriesId: Long,
        @Query("page") page: Int
    ): ResultResponse<ReviewRemoteDto>

    @GET("tv/{seriesId}/similar")
    suspend fun getSimilarSeries(
        @Path("seriesId") seriesId: Long,
        @Query("page") page: Int
    ): ResultResponse<SeriesRemoteDto>

    @GET("tv/{seriesId}/credits")
    suspend fun getSeriesTopCast(
        @Path("seriesId") seriesId: Long,
        @Query("page") page: Int
    ): CreditResponse

    @GET("tv/{seriesId}")
    suspend fun getSeriesSeasons(
        @Path("seriesId") seriesId: Long
    ): SeriesResponse

    @GET("tv/{seriesId}/season/{seasonNumber}")
    suspend fun getEpisodes(
        @Path("seriesId") seriesId: Long,
        @Path("seasonNumber") seasonNumber: Int
    ): SeasonResponse

    @GET("movie/{seriesId}/videos")
    suspend fun getVideoKey(
        @Path("seriesId") seriesId: Long
    ): VideoResponse

    @GET("discover/tv")
    suspend fun getTopRatingSeries(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String? = null,
        @Query("sort_by") sortBy: String = "vote_average.desc",
        @Query("vote_count.gte") voteCountGte: Int = 200,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US"
    ): ResultResponse<SeriesRemoteDto>

    @GET("discover/tv")
    suspend fun getOnTvSeries(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String? = null,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("air_date.gte") minDate: String? = null,
        @Query("air_date.lte") maxDate: String? = null,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US"
    ): ResultResponse<SeriesRemoteDto>

    @GET("discover/tv")
    suspend fun getAiringTodaySeries(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String? = null,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("air_date.gte") minDate: String? = null,
        @Query("air_date.lte") maxDate: String? = null,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US"
    ): ResultResponse<SeriesRemoteDto>


    @GET("discover/tv")
    suspend fun getTrendingSeries(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String? = null,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("vote_count.gte") voteCountGte: Int = 50,
        @Query("air_date.gte") minDate: String? = null,
        @Query("air_date.lte") maxDate: String? = null,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("language") language: String = "en-US"
    ): ResultResponse<SeriesRemoteDto>

    @GET("discover/tv")
    suspend fun getMoreRecommendedSeries(
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String = "vote_count.desc",
        @Query("with_genres") withGenres: String? = null
    ): ResultResponse<SeriesRemoteDto>

    @GET("discover/tv")
    suspend fun getFreeToWatchSeries(
        @Query("page") page: Int,
        @Query("with_watch_providers") free: String = "free", // TODO: find better way
        @Query("with_genres") withGenres: String? = null
    ): ResultResponse<SeriesRemoteDto>

    @GET("discover/tv")
    suspend fun getSeriesByCategory(
        @Query("with_genres") categoryId: String,
        @Query("page") page: Int,
    ): ResultResponse<SeriesRemoteDto>

    @GET("genre/tv/list")
    suspend fun getSeriesGenres(
    ): GenreResponse

    @GET("tv/popular")
    suspend fun getPopularSeries(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String? = null
    ): ResultResponse<SeriesRemoteDto>

    @GET("discover/tv")
    suspend fun getAllSeries(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String? = null,
        @Query("sort_by") sortBy: String? = null
    ): ResultResponse<SeriesRemoteDto>
}