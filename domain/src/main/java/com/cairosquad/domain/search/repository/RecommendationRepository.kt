package com.cairosquad.domain.search.repository

import com.cairosquad.entity.Movie

interface RecommendationRepository {
    suspend fun getForYouMovies(): List<Movie>

    suspend fun getExploreMoreMovies(): List<Movie>
}