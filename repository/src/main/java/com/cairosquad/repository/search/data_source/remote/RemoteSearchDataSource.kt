package com.cairosquad.repository.search.data_source.remote

import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto

interface RemoteSearchDataSource {
    suspend fun getArtists(query: String,page:Int): List<ArtistRemoteDto>
}