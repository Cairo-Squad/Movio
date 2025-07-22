package com.cairosquad.domain.usecase.movies

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Movie

class GetMoreRecommendedMoviesUseCase(
    private val movieRepository: MoviesRepository
) {
    suspend fun getMoreRecommendedMovies(page:Int, categoryId: String? = null ) : List<Movie>{
        return movieRepository.getMoreRecommendedMovies(page,categoryId)
    }


}