package com.cairosquad.repository.search.data_source.remote

import com.cairosquad.repository.search.data_source.remote.dto.ArtistDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieDto
import com.cairosquad.repository.search.data_source.remote.dto.SeriesDto

interface RemoteSearchDataSource {
    suspend fun searchMovies(query: String): List<MovieDto>

    suspend fun searchSeries(query: String): List<SeriesDto>

    suspend fun searchArtists(query: String): List<ArtistDto>
}