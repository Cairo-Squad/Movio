package com.cairosquad.domain.search.repository

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

interface ArtistDetailsRepository {
    suspend fun getArtistById(artistId: Long): Artist

    suspend fun getMoviesThatArtistIsKnownFor(artistId: Long): List<Movie>

    suspend fun getSeriesThatArtistIsKnownFor(artistId: Long): List<Series>
}