package com.cairosquad.remote.search

import com.cairosquad.repository.search.data_source.remote.RemoteMovieDiscoveryDataSource
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto

class RemoteMovieDiscoveryDataSourceImpl(
    private val searchApiService: SearchApiService
) : RemoteMovieDiscoveryDataSource {
    override suspend fun getPersonalizedMovies(page: Int): List<MovieRemoteDto> {
        return searchApiService.getPersonalizedMovies(page)
            .filter { it.id != null }
    }

    override suspend fun getSuggestedMovies(): List<MovieRemoteDto> {
        return searchApiService.getSuggestedMovies()
            .filter { it.id != null }
    }
}