package com.cairosquad.domain.usecase.movies

import com.cairosquad.domain.repository.MoviesRepository

class GetMoviesByCategoryUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun getMoviesByCategory( page : Int,categoryId : String) =
        moviesRepository.getMoviesByCategory(page,categoryId)
}