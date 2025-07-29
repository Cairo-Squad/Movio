package com.cairosquad.remote.artists

import com.cairosquad.repository.artists.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.CreditResponse
import com.cairosquad.repository.utils.sharedDto.remote.ResultResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArtistsApiService {
    @GET("search/person")
    suspend fun getArtistsByQuery(
        @Query("query") query: String,
        @Query("page") page: Int
    ): ResultResponse<ArtistRemoteDto>

    @GET("person/{id}")
    suspend fun getArtistById(
        @Path("id") id: Long
    ): ArtistRemoteDto

    @GET("movie/{movieId}/credits")
    suspend fun getMovieTopCast(
        @Path("movieId") movieId: Long,
        @Query("page") page: Int
    ): CreditResponse

    @GET("tv/{seriesId}/credits")
    suspend fun getSeriesTopCast(
        @Path("seriesId") seriesId: Long,
        @Query("page") page: Int
    ): CreditResponse
}
