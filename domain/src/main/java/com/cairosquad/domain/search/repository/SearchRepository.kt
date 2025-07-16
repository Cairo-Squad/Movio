package com.cairosquad.domain.search.repository

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

interface SearchRepository {
    suspend fun getSeries(query: String,page:Int): List<Series>

    suspend fun getMovies(query: String,page:Int): List<Movie>

    suspend fun getArtists(query: String,page:Int): List<Artist>
}
