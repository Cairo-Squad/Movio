package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.ArtistsRepository
import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

class ManageArtistUseCase(
    private val artistsRepository: ArtistsRepository,
    private val searchRepository: SearchRepository,
) {
    suspend fun getArtistsByQuery(query: String, page: Int ): List<Artist> {
        return artistsRepository.getArtistsByQuery(query,page).also {
            searchRepository.addQuery(query)
        }
    }
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