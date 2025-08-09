package com.cairosquad.remote.series

import com.cairosquad.remote.artists.SeriesListResponse
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.VideoResponse
import com.cairosquad.repository.series.data_source.remote.dto.SeasonResponse
import com.cairosquad.repository.series.data_source.remote.dto.SeriesDetailsRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesResponse
import com.cairosquad.repository.utils.sharedDto.remote.GenreResponse
import com.cairosquad.repository.utils.sharedDto.remote.ResultResponse
import com.cairosquad.repository.utils.sharedDto.remote.StatusResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SeriesApiService {

    @GET("tv/{seriesId}")
    suspend fun getSeriesById(
        @Path("seriesId") id: Long
    ): SeriesDetailsRemoteDto

    @GET("tv/{seriesId}/reviews")
    suspend fun getSeriesReviews(
        @Path("seriesId") seriesId: Long,
        @Query("page") page: Int
    ): ResultResponse<ReviewRemoteDto>

    @GET("person/{id}/tv_credits")
    suspend fun getSeriesOfArtist(
        @Path("id") artistId: Long
    ): SeriesListResponse

    @GET("tv/{seriesId}/similar")
    suspend fun getSimilarSeries(
        @Path("seriesId") seriesId: Long,
        @Query("page") page: Int
    ): ResultResponse<SeriesRemoteDto>

    @GET("tv/{seriesId}")
    suspend fun getSeriesSeasons(
        @Path("seriesId") seriesId: Long
    ): SeriesResponse

    @GET("tv/{seriesId}/season/{seasonNumber}")
    suspend fun getEpisodes(
        @Path("seriesId") seriesId: Long,
        @Path("seasonNumber") seasonNumber: Int
    ): SeasonResponse

    @GET("tv/{seriesId}/videos")
    suspend fun getVideoKey(
        @Path("seriesId") seriesId: Long
    ): VideoResponse

    @GET("discover/tv?sort_by=vote_average.desc&include_adult=false&vote_count.gte=200")
    suspend fun getTopRatingSeries(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: Long?,
    ): ResultResponse<SeriesRemoteDto>

    @GET("discover/tv?sort_by=popularity.desc&include_adult=false")
    suspend fun getOnTvSeries(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: Long?,
        @Query("air_date.gte") minDate: String?,
        @Query("air_date.lte") maxDate: String?,
    ): ResultResponse<SeriesRemoteDto>

    @GET("discover/tv?sort_by=popularity.desc&include_adult=false")
    suspend fun getAiringTodaySeries(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: Long?,
        @Query("air_date.gte") minDate: String?,
        @Query("air_date.lte") maxDate: String?,
    ): ResultResponse<SeriesRemoteDto>


    @GET("discover/tv?sort_by=popularity.desc&include_adult=false&vote_count.gte=50")
    suspend fun getTrendingSeries(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: Long?,
        @Query("air_date.gte") minDate: String?,
        @Query("air_date.lte") maxDate: String?,
    ): ResultResponse<SeriesRemoteDto>

    @GET("discover/tv?sort_by=popularity.desc")
    suspend fun getMoreRecommendedSeries(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: Long?
    ): ResultResponse<SeriesRemoteDto>

    @GET("discover/tv?with_watch_providers=free")
    suspend fun getFreeToWatchSeries(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: Long?
    ): ResultResponse<SeriesRemoteDto>

    @GET("discover/tv")
    suspend fun getSeriesByCategory(
        @Query("with_genres") genreId: Long,
        @Query("page") page: Int,
    ): ResultResponse<SeriesRemoteDto>

    @GET("genre/tv/list")
    suspend fun getSeriesGenres(
    ): GenreResponse

    @GET("tv/popular")
    suspend fun getPopularSeries(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: Long?
    ): ResultResponse<SeriesRemoteDto>

    @GET("discover/tv")
    suspend fun getAllSeries(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: Long?,
        @Query("sort_by") sortBy: String?
    ): ResultResponse<SeriesRemoteDto>

    @GET("search/tv")
    suspend fun getSeriesByQuery(
        @Query("query") query: String,
        @Query("page") page: Int
    ): ResultResponse<SeriesRemoteDto>

    @POST("tv/{series_id}/rating")
    suspend fun addSeriesRating(
        @Path("series_id") seriesId: Long,
        @Query("value") rating: Float
    ): StatusResponse
}