package com.cairosquad.remote.search

import com.cairosquad.remote.utils.retrofit.ApiServiceProvider
import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.search.data_source.remote.RemoteMovieDiscoveryDataSource
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto

class RemoteMovieDiscoveryDataSourceImpl(
    private val apiServiceProvider: ApiServiceProvider
) : RemoteMovieDiscoveryDataSource {
    override suspend fun getPersonalizedMovies(page: Int): List<MovieRemoteDto> {
        return safeCallApi { apiServiceProvider.getSearchApiService().getPersonalizedMovies(page) }
            .results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }

    override suspend fun getSuggestedMovies(): List<MovieRemoteDto> {
        return safeCallApi { apiServiceProvider.getSearchApiService().getSuggestedMovies() }
            .results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }
}