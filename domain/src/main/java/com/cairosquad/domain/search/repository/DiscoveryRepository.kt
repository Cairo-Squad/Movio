package com.cairosquad.domain.search.repository

import com.cairosquad.entity.Movie

interface DiscoveryRepository {
    suspend fun getPersonalizedMovies(): List<Movie>

    suspend fun getSuggestedMovies(): List<Movie>
}