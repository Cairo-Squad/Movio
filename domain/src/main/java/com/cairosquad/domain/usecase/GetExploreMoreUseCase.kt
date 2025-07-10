package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.entity.Movie

class GetExploreMoreUseCase(  private val searchRepository: SearchRepository,) {
    suspend fun getExploreMoreMovies(): List<Movie> = searchRepository.getExploreMoreMovies()
}