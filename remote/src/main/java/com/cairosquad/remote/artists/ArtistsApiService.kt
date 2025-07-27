package com.cairosquad.remote.artists

import com.cairosquad.repository.artists.data_source.remote.dto.ArtistRemoteDto
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

    @GET("person/{id}/movie_credits")
    suspend fun getMoviesOfArtist(
        @Path("id") artistId: Long
    ): MoviesListResponse

    @GET("person/{id}/tv_credits")
    suspend fun getSeriesOfArtist(
        @Path("id") artistId: Long
    ): SeriesListResponse
}
