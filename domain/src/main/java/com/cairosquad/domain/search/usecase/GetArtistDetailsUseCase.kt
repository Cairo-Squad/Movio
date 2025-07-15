package com.cairosquad.domain.search.usecase

import com.cairosquad.domain.search.repository.ArtistDetailsRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

class GetArtistDetailsUseCase(
    private val artistDetailsRepository: ArtistDetailsRepository
) {
    suspend fun getArtistById(artistId: Long): Artist {
        return artistDetailsRepository.getArtistById(artistId)
    }

    suspend fun getMoviesThatArtistIsKnownFor(artistId: Long): List<Movie> {
        return artistDetailsRepository.getMoviesThatArtistIsKnownFor(artistId)
    }

    suspend fun getSeriesThatArtistIsKnownFor(artistId: Long): List<Series> {
        return artistDetailsRepository.getSeriesThatArtistIsKnownFor(artistId)
    }
}