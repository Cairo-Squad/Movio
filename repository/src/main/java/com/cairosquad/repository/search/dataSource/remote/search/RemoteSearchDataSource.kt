package com.cairosquad.repository.search.dataSource.remote.search

import com.cairosquad.repository.search.dataSource.remote.search.dto.ArtistDto
import com.cairosquad.repository.search.dataSource.remote.search.dto.MovieDto
import com.cairosquad.repository.search.dataSource.remote.search.dto.SeriesDto

interface RemoteSearchDataSource {
    suspend fun searchMovies(query: String): List<MovieDto>

    suspend fun searchSeries(query: String): List<SeriesDto>

    suspend fun searchArtists(query: String): List<ArtistDto>
}