package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.SearchHistoryRepository
import com.cairosquad.domain.search.repository.SearchRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

class SearchUseCase(
    private val searchRepository: SearchRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend fun searchSeries(query: String): List<Series> {
        val result = searchRepository.searchSeries(query)
        searchHistoryRepository.addQuery(query)
        return result
    }

    suspend fun searchMovies(query: String): List<Movie> {
        val result = searchRepository.searchMovies(query)
        searchHistoryRepository.addQuery(query)
        return result
    }

    suspend fun searchArtists(query: String): List<Artist> {
        val result = searchRepository.searchArtists(query)
        searchHistoryRepository.addQuery(query)
        return result
    }
}