package com.cairosquad.domain.search.repository

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

interface SearchRepository {
    suspend fun searchSeries(query: String): List<Series>

    suspend fun searchMovies(query: String): List<Movie>

    suspend fun searchArtists(query: String): List<Artist>

    suspend fun getForYouMovies(): List<Movie>

    suspend fun getExploreMoreMovies(): List<Movie>
}
