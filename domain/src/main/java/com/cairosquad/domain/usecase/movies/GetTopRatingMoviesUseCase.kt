package com.cairosquad.domain.usecase.movies

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Movie

class GetTopRatingMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend fun getTopRatingMovies(page: Int, categoryId: String? = null) : List<Movie>{
        return moviesRepository.getTopRatingMovies(page,categoryId)
    }
}