package com.cairosquad.remote.search

import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.search.data_source.remote.RemoteMovieDiscoveryDataSource
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto

class RemoteMovieDiscoveryDataSourceImpl(
    private val searchApiService: SearchApiService
) : RemoteMovieDiscoveryDataSource {
    override suspend fun getPersonalizedMovies(page: Int): List<MovieRemoteDto> {
        return safeCallApi { searchApiService.getPersonalizedMovies(page) }
            .results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }

    override suspend fun getSuggestedMovies(): List<MovieRemoteDto> {
        return safeCallApi { searchApiService.getSuggestedMovies() }
            .results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }
}