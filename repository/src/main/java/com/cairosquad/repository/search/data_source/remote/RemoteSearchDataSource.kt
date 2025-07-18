package com.cairosquad.repository.search.data_source.remote

import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto

interface RemoteSearchDataSource {
    suspend fun getMovies(query: String,page:Int): List<MovieRemoteDto>

    suspend fun getSeries(query: String,page:Int): List<SeriesRemoteDto>

    suspend fun getArtists(query: String,page:Int): List<ArtistRemoteDto>
}