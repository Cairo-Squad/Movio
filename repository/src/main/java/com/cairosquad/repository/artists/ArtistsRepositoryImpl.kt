package com.cairosquad.repository.artists

import com.cairosquad.domain.repository.ArtistsRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.artists.data_source.ArtistsRemoteDataSource
import com.cairosquad.repository.search.data_source.remote.dto.toEntity
import com.cairosquad.repository.search.data_source.remote.dto.toEntityList
import com.cairosquad.repository.utils.mappers.tryToCall

class ArtistsRepositoryImpl(
    private val artistsRemoteDataSource: ArtistsRemoteDataSource,
) : ArtistsRepository {
    override suspend fun getArtist(artistId: Long): Artist {
        return tryToCall {
                artistsRemoteDataSource.getArtist(artistId).toEntity()
        }
    }

    override suspend fun getMoviesOfArtist(artistId: Long): List<Movie> {
        return tryToCall {
            artistsRemoteDataSource.getMoviesOfArtist(artistId).toEntityList()
        }
    }

    override suspend fun getSeriesOfArtist(artistId: Long): List<Series> {
        return tryToCall {
            artistsRemoteDataSource.getSeriesOfArtist(artistId).toEntityList()
        }
    }
}