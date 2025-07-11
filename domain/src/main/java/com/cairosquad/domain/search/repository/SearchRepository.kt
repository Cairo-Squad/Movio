package com.cairosquad.domain.search.repository

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

interface SearchRepository {
    suspend fun getSeries(query: String): List<Series>

    suspend fun getMovies(query: String): List<Movie>

    suspend fun getArtists(query: String): List<Artist>
}
