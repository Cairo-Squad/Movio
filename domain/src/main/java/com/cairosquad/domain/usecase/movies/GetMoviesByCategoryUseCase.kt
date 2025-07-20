package com.cairosquad.domain.usecase.movies

import com.cairosquad.domain.repository.MoviesRepository

class GetMoviesByCategoryUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun getMoviesByCategory(categoryId : String , page : Int) =
        moviesRepository.getMoviesByCategory(categoryId, page)
}