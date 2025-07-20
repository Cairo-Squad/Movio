package com.cairosquad.remote.series

import com.cairosquad.repository.movie.data_source.remote.dto.CreditResponse
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeasonResponse
import com.cairosquad.repository.series.data_source.remote.dto.SeriesDetailsRemoteDto
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
    ): List<ReviewRemoteDto>

    @GET("tv/{seriesId}/similar")
    suspend fun getSimilarSeries(
        @Path("seriesId") seriesId: Long,
        @Query("page") page: Int
    ): List<SeriesRemoteDto>

    @GET("tv/{seriesId}/credits")
    suspend fun getSeriesTopCast(
        @Path("seriesId") seriesId: Long,
        @Query("page") page: Int
    ): List<CreditResponse>

    @GET("tv/{seriesId}")
    suspend fun getSeriesSeasons(
        @Path("seriesId") seriesId: Long
    ): List<SeriesResponse>

    @GET("tv/{seriesId}/season/{seasonNumber}")
    suspend fun getEpisodes(
        @Path("seriesId") seriesId: Long,
        @Path("seasonNumber") seasonNumber: Int
    ): List<SeasonResponse>

}