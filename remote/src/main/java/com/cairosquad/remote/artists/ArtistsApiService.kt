package com.cairosquad.remote.artists


import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ResultResponse
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ArtistsApiService {
    @GET("person/{id}")
    suspend fun getArtist(
        @Path("id") artistId: Long
    ): ArtistRemoteDto

    @GET("person/{id}/movie_credits")
    suspend fun getMoviesOfArtist(
        @Path("id") artistId: Long
    ): ResultResponse<MovieRemoteDto>

    @GET("person/{id}/tv_credits")
    suspend fun getSeriesOfArtist(
        @Path("id") artistId: Long
    ): ResultResponse<SeriesRemoteDto>

}