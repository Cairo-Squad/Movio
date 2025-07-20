package com.cairosquad.remote.search

import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {

    @GET("search/movie")
    suspend fun getMovies(
        @Query("query") query: String,
        @Query("page") page: Int
    ): List<MovieRemoteDto>

    @GET("search/tv")
    suspend fun getSeries(
        @Query("query") query: String,
        @Query("page") page: Int
    ): List<SeriesRemoteDto>

    @GET("search/person")
    suspend fun getArtists(
        @Query("query") query: String,
        @Query("page") page: Int
    ): List<ArtistRemoteDto>

    @GET("movie/top_rated")
    suspend fun getPersonalizedMovies(
        @Query("page") page: Int
    ): List<MovieRemoteDto>

    @GET("movie/now_playing")
    suspend fun getSuggestedMovies(): List<MovieRemoteDto>
}