package com.cairosquad.domain.search.repository

import com.cairosquad.entity.Movie

interface MovieDiscoveryRepository {
    suspend fun getPersonalizedMovies(): List<Movie>

    suspend fun getSuggestedMovies(): List<Movie>
}