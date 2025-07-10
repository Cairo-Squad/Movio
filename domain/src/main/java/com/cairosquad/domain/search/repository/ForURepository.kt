package com.cairosquad.domain.search.repository

import com.cairosquad.entity.Movie

interface ForURepository {
    suspend fun getForYouMovies(): List<Movie>
}