package com.cairosquad.domain.usecase.artists

import com.cairosquad.domain.repository.ArtistRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

class GetArtistDetailsUseCase(
    private val artistsRepository: ArtistRepository
) {
    suspend fun getArtist(artistId: Long): Artist {
        return artistsRepository.getArtist(artistId)
    }

    suspend fun getMoviesOfArtist(artistId: Long): List<Movie> {
        return artistsRepository.getMoviesOfArtist(artistId)
    }

    suspend fun getSeriesOfArtist(artistId: Long): List<Series> {
        return artistsRepository.getSeriesOfArtist(artistId)
    }
}