package com.cairosquad.domain.usecase

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

class SearchUseCase {
    suspend fun searchSeries(query: String): List<Series> {
        TODO()
    }

    suspend fun searchMovies(query: String): List<Movie> {
        TODO()
    }

    suspend fun searchArtists(query: String): List<Artist> {
        TODO()
    }
}