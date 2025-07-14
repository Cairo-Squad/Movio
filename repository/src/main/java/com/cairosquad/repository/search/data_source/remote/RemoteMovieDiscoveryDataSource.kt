package com.cairosquad.repository.search.data_source.remote

import com.cairosquad.repository.search.data_source.remote.dto.MovieDto

interface RemoteMovieDiscoveryDataSource {
    suspend fun getPersonalizedMovies(): List<MovieDto>

    suspend fun getSuggestedMovies(): List<MovieDto>
}