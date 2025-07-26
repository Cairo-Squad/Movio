package com.cairosquad.remote.search

import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.search.data_source.remote.RemoteSearchDataSource
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto

class RemoteSearchDataSourceImpl(
    private val searchApiService: SearchApiService
) : RemoteSearchDataSource {

    override suspend fun getSeries(query: String, page: Int): List<SeriesRemoteDto> {
        return safeCallApi { searchApiService.getSeries(query, page) }
            .results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }

    override suspend fun getArtists(query: String, page: Int): List<ArtistRemoteDto> {
        return safeCallApi { searchApiService.getArtists(query, page) }
            .results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }

}