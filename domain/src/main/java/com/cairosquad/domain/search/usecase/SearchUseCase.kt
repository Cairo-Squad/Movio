package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.SearchHistoryRepository
import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

class SearchUseCase(
    private val searchRepository: SearchRepository,
    private val recentSearchRepository: SearchHistoryRepository
) {
    suspend fun getSeries(query: String): List<Series> {
        val result = searchRepository.getSeries(query)
        recentSearchRepository.addQuery(query)
        return result
    }

    suspend fun getMovies(query: String): List<Movie> {
        val result = searchRepository.getMovies(query)
        recentSearchRepository.addQuery(query)
        return result
    }

    suspend fun getArtists(query: String): List<Artist> {
        val result = searchRepository.getArtists(query)
        recentSearchRepository.addQuery(query)
        return result
    }
}