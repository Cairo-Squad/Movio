package com.cairosquad.remote.search

import com.cairosquad.remote.utils.retrofit.ApiServiceProvider
import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.search.data_source.remote.RemoteSearchDataSource
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto

class RemoteSearchDataSourceImpl(
    private val apiServiceProvider: ApiServiceProvider
) : RemoteSearchDataSource {
    override suspend fun getMovies(query: String, page: Int): List<MovieRemoteDto> {
        return safeCallApi { apiServiceProvider.getSearchApiService().getMovies(query, page) }
            .results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }

    override suspend fun getSeries(query: String, page: Int): List<SeriesRemoteDto> {
        return safeCallApi { apiServiceProvider.getSearchApiService().getSeries(query, page) }
            .results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }

    override suspend fun getArtists(query: String, page: Int): List<ArtistRemoteDto> {
        return safeCallApi { apiServiceProvider.getSearchApiService().getArtists(query, page) }
            .results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }

}