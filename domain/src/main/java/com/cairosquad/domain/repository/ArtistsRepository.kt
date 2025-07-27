package com.cairosquad.domain.repository

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

interface ArtistsRepository {
    suspend fun getArtistsByQuery(query: String,page:Int): List<Artist>

    suspend fun getArtistById(id: Long): Artist

    suspend fun getMoviesOfArtist(artistId: Long): List<Movie>

    suspend fun getSeriesOfArtist(artistId: Long): List<Series>
}