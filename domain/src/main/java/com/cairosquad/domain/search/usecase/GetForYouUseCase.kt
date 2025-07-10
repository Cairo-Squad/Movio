package com.cairosquad.domain.usecase

import com.cairosquad.domain.search.repository.ForURepository
import com.cairosquad.entity.Movie

class GetForYouUseCase(  private val movieRepository: MovieRepository,) {
    suspend fun getForYouMovies(): List<Movie> =movieRepository .getForYouMovies()
}