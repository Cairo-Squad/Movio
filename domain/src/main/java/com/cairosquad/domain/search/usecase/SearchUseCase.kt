package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

class SearchUseCase(
    private val searchRepository: SearchRepository
) {
    suspend fun searchSeries(query: String): List<Series> {
        return searchRepository.searchSeries(query)
    }

    suspend fun searchMovies(query: String): List<Movie> {
        return searchRepository.searchMovies(query)
    }

    suspend fun searchArtists(query: String): List<Artist> {
        return searchRepository.searchArtists(query)
    }
}