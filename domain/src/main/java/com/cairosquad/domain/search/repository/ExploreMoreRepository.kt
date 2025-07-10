package com.cairosquad.domain.search.repository

import com.cairosquad.entity.Movie

interface ExploreMoreRepository {
    suspend fun getExploreMoreMovies(): List<Movie>
}