package com.cairosquad.domain.usecase.search

import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

class SearchUseCase(
    private val searchRepository: SearchRepository,
    private val moviesRepository: MoviesRepository,
    private val seriesRepository: SeriesRepository
) {
    suspend fun getSeries(query: String, page: Int): List<Series> {
        return seriesRepository.getSeriesByQuery(query,page).also {
            searchRepository.addQuery(query)
        }
    }

    suspend fun getMovies(query: String, page: Int ): List<Movie> {
        return moviesRepository.getMoviesByQuery(query,page).also {
            searchRepository.addQuery(query)
        }
    }

    suspend fun getArtists(query: String, page: Int ): List<Artist> {
        return searchRepository.getArtists(query,page).also {
            searchRepository.addQuery(query)
        }
    }
}
