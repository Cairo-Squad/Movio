package com.cairosquad.domain.usecase.artists

import com.cairosquad.domain.repository.ArtistsRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

class GetArtistDetailsUseCase(
    private val artistsRepository: ArtistsRepository
) {
    suspend fun getArtist(artistId: Long): Artist {
        return artistsRepository.getArtistById(artistId)
    }

    suspend fun getMoviesOfArtist(artistId: Long): List<Movie> {
        return artistsRepository.getMoviesOfArtist(artistId)
    }

    suspend fun getSeriesOfArtist(artistId: Long): List<Series> {
        return artistsRepository.getSeriesOfArtist(artistId)
    }
}