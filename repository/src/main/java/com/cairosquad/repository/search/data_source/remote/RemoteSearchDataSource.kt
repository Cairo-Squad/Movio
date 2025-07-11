package com.cairosquad.repository.search.data_source.remote

import com.cairosquad.repository.search.data_source.remote.dto.ArtistDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieDto
import com.cairosquad.repository.search.data_source.remote.dto.SeriesDto

interface RemoteSearchDataSource {
    suspend fun getMovies(query: String): List<MovieDto>

    suspend fun getSeries(query: String): List<SeriesDto>

    suspend fun getArtists(query: String): List<ArtistDto>
}