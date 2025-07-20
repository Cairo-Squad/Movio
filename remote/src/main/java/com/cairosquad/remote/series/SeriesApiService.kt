package com.cairosquad.remote.series

import com.cairosquad.repository.movie.data_source.remote.dto.CreditResponse
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ResultResponse
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeasonResponse
import com.cairosquad.repository.series.data_source.remote.dto.SeriesDetailsRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SeriesApiService {

    @GET("tv/{seriesId}")
    suspend fun getSeries(
        @Path("seriesId") seriesId: Long
    ): Response<SeriesDetailsRemoteDto>

    @GET("tv/{seriesId}/reviews")
    suspend fun getSeriesReviews(
        @Path("seriesId") seriesId: Long,
        @Query("page") page: Int
    ): Response<ResultResponse<ReviewRemoteDto>>

    @GET("tv/{seriesId}/similar")
    suspend fun getSimilarSeries(
        @Path("seriesId") seriesId: Long,
        @Query("page") page: Int
    ): Response<ResultResponse<SeriesRemoteDto>>

    @GET("tv/{seriesId}/credits")
    suspend fun getSeriesTopCast(
        @Path("seriesId") seriesId: Long,
        @Query("page") page: Int
    ): Response<CreditResponse>

    @GET("tv/{seriesId}")
    suspend fun getSeriesSeasons(
        @Path("seriesId") seriesId: Long
    ): Response<SeriesResponse>

    @GET("tv/{seriesId}/season/{seasonNumber}")
    suspend fun getEpisodes(
        @Path("seriesId") seriesId: Long,
        @Path("seasonNumber") seasonNumber: Int
    ): Response<SeasonResponse>

}