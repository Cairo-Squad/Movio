package com.cairosquad.domain.search.repository

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

interface ArtistRepository {
    suspend fun getArtist(artistId: Long): Artist

    suspend fun getMoviesOfArtist(artistId: Long): List<Movie>

    suspend fun getSeriesOfArtist(artistId: Long): List<Series>
}