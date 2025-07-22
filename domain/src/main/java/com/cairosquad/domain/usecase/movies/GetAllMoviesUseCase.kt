package com.cairosquad.domain.usecase.movies

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.entity.Movie

class GetAllMoviesUseCase (
    private val moviesRepository: MoviesRepository
){
    suspend fun getAllMovies(page: Int, categoryId: String? = null) : List<Movie> {
        return moviesRepository.getAllMovies(page,categoryId)
    }
}