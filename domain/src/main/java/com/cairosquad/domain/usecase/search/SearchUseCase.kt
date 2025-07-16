package com.cairosquad.domain.usecase.search

import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

class SearchUseCase(
    private val searchRepository: SearchRepository,
) {
    suspend fun getSeries(query: String, page: Int = 1): List<Series> {
        return searchRepository.getSeries(query).also {
            searchRepository.addQuery(query)
        }
    }

    suspend fun getMovies(query: String, page: Int = 1): List<Movie> {
        return searchRepository.getMovies(query).also {
            searchRepository.addQuery(query)
        }
    }

    suspend fun getArtists(query: String, page: Int = 1): List<Artist> {
        return searchRepository.getArtists(query).also {
            searchRepository.addQuery(query)
        }
    }
}
