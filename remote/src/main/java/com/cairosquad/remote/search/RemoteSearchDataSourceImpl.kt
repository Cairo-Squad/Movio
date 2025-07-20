package com.cairosquad.remote.search

import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.search.data_source.remote.RemoteSearchDataSource
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto

class RemoteSearchDataSourceImpl(
    private val searchApiService: SearchApiService
) : RemoteSearchDataSource {
    override suspend fun getMovies(query: String, page: Int): List<MovieRemoteDto> {
        return searchApiService.getMovies(query, page).filter { it.id != null }
    }

    override suspend fun getSeries(query: String, page: Int): List<SeriesRemoteDto> {
        return searchApiService.getSeries(query, page).filter { it.id != null }
    }

    override suspend fun getArtists(query: String, page: Int): List<ArtistRemoteDto> {
        return searchApiService.getArtists(query, page).filter { it.id != null }
    }
}