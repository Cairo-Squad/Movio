package com.cairosquad.domain.repository

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

<<<<<<<< HEAD:domain/src/main/java/com/cairosquad/domain/repository/ArtistRepository.kt
interface ArtistRepository {
    ========
    interface ArtistsRepository {
        >>>>>>>> refs/heads/develop:domain/src/main/java/com/cairosquad/domain/repository/ArtistsRepository.kt
    suspend fun getArtist(artistId: Long): Artist

    suspend fun getMoviesOfArtist(artistId: Long): List<Movie>

    suspend fun getSeriesOfArtist(artistId: Long): List<Series>
}