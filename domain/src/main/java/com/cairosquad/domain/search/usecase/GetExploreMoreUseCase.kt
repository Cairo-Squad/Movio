package com.cairosquad.domain.usecase

import com.cairosquad.domain.search.repository.ExploreMoreRepository
import com.cairosquad.entity.Movie

class GetExploreMoreUseCase(  private val exploremoreRepository: ExploreMoreRepository,) {
    suspend fun getExploreMoreMovies(): List<Movie> = exploremoreRepository.getExploreMoreMovies()
}