package com.cairosquad.domain.usecase.search

import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.domain.repository.UserCategoryPreferenceRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

class SearchUseCase(
    private val searchRepository: SearchRepository,
    private val getUserCategoryPreferenceUseCase: GetUserCategoryPreferenceUseCase
) {
    suspend fun getSeries(query: String, page: Int = 1): List<Series> {
        val res = getUserCategoryPreferenceUseCase()
        val result = searchRepository.getSeries(query).also {
            searchRepository.addQuery(query)
        }

        return if (res != null) {
            result.sortedBy { series -> series.genres.any { it == res } }
        } else {
            result
        }
    }

    suspend fun getMovies(query: String, page: Int = 1): List<Movie> {
        val res = getUserCategoryPreferenceUseCase()
        val result = searchRepository.getMovies(query).also {
            searchRepository.addQuery(query)
        }

        return if (res != null) {
            result.sortedBy { movie -> movie.genres.any { it == res } }
        } else {
            result
        }
    }

    suspend fun getArtists(query: String, page: Int = 1): List<Artist> {
        return searchRepository.getArtists(query).also {
            searchRepository.addQuery(query)
        }
    }
}
