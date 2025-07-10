package com.cairosquad.domain.usecase

import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.entity.Movie

class GetForYouUseCase(  private val searchRepository: SearchRepository,) {
    suspend fun getForYouMovies(): List<Movie> = searchRepository.getForYouMovies()
}