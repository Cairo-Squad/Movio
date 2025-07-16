package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.ArtistRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

class GetArtistDetailsUseCase(
    private val artistRepository: ArtistRepository
) {
    suspend fun getArtist(artistId: Long): Artist {
        return artistRepository.getArtist(artistId)
    }

    suspend fun getMoviesOfArtist(artistId: Long): List<Movie> {
        return artistRepository.getMoviesOfArtist(artistId)
    }

    suspend fun getSeriesOfArtist(artistId: Long): List<Series> {
        return artistRepository.getSeriesOfArtist(artistId)
    }
}