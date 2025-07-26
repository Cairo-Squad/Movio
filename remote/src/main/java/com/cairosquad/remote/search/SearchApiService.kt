package com.cairosquad.remote.search

import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ResultResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {
    @GET("search/person")
    suspend fun getArtists(
        @Query("query") query: String,
        @Query("page") page: Int
    ): ResultResponse<ArtistRemoteDto>
}