package com.cairosquad.remote.search

import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.search.data_source.remote.RemoteSearchDataSource
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto

class RemoteSearchDataSourceImpl(
    private val searchApiService: SearchApiService
) : RemoteSearchDataSource {
    override suspend fun getArtists(query: String, page: Int): List<ArtistRemoteDto> {
        return safeCallApi { searchApiService.getArtists(query, page) }
            .results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }
}