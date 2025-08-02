package com.cairosquad.domain.usecase

import com.cairosquad.domain.repository.ArtistsRepository
import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.domain.repository.SeriesRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import javax.inject.Inject

class ManageArtistUseCase @Inject constructor(
    private val artistsRepository: ArtistsRepository,
    private val searchRepository: SearchRepository,
    private val moviesRepository: MoviesRepository,
    private val seriesRepository: SeriesRepository
) {
    suspend fun getArtistsByQuery(query: String, page: Int ): List<Artist> {
        return artistsRepository.getArtistsByQuery(query,page).also {
            searchRepository.addQuery(query)
        }
    }

    suspend fun getArtistById(id: Long): Artist {
        return artistsRepository.getArtistById(id)
    }

    suspend fun getMoviesOfArtist(artistId: Long): List<Movie> {
        return moviesRepository.getMoviesOfArtist(artistId)
    }

    suspend fun getSeriesOfArtist(artistId: Long): List<Series> {
        return seriesRepository.getSeriesOfArtist(artistId)
    }
}