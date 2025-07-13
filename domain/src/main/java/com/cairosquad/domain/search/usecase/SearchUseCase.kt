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
        return searchRepository.getSeries(query).also {
            recentSearchRepository.addQuery(query)
        }
    }

    suspend fun getMovies(query: String): List<Movie> {
        return searchRepository.getMovies(query).also {
            recentSearchRepository.addQuery(query)
        }
    }

    suspend fun getArtists(query: String): List<Artist> {
        return searchRepository.getArtists(query).also {
            recentSearchRepository.addQuery(query)
        }
    }
}
