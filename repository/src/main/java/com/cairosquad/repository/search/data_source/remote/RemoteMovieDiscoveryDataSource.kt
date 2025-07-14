package com.cairosquad.repository.search.data_source.remote

import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto


interface RemoteMovieDiscoveryDataSource {
    suspend fun getPersonalizedMovies(): List<MovieRemoteDto>

    suspend fun getSuggestedMovies(): List<MovieRemoteDto>
}