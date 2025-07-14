package com.cairosquad.repository.search.data_source.remote

import com.cairosquad.repository.search.data_source.remote.dto.ApiArtistDto
import com.cairosquad.repository.search.data_source.remote.dto.ApiMovieDto
import com.cairosquad.repository.search.data_source.remote.dto.ApiSeriesDto

interface RemoteSearchDataSource {
    suspend fun getMovies(query: String): List<ApiMovieDto>

    suspend fun getSeries(query: String): List<ApiSeriesDto>

    suspend fun getArtists(query: String): List<ApiArtistDto>
}