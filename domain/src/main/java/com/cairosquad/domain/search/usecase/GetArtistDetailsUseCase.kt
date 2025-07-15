package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.ArtistDetailsRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

class GetArtistDetailsUseCase(
    private val artistDetailsRepository: ArtistDetailsRepository
) {
    suspend fun getArtist(artistId: Long): Artist {
        return artistDetailsRepository.getArtist(artistId)
    }

    suspend fun getMoviesOfArtist(artistId: Long): List<Movie> {
        return artistDetailsRepository.getMoviesOfArtist(artistId)
    }

    suspend fun getSeriesOfArtist(artistId: Long): List<Series> {
        return artistDetailsRepository.getSeriesOfArtist(artistId)
    }
}