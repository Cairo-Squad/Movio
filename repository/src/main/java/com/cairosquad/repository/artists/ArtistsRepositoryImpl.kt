package com.cairosquad.repository.artists

import com.cairosquad.domain.repository.ArtistsRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.repository.artists.data_source.remote.ArtistsRemoteDataSource
import com.cairosquad.repository.artists.data_source.remote.toEntity
import com.cairosquad.repository.artists.data_source.remote.toEntityList
import com.cairosquad.repository.movie.data_source.remote.toEntityList
import com.cairosquad.repository.series.data_source.remote.toEntityList
import com.cairosquad.repository.utils.mappers.tryToCall

class ArtistsRepositoryImpl(
    private val artistsRemoteDataSource: ArtistsRemoteDataSource,
) : ArtistsRepository {
    override suspend fun getArtistsByQuery(
        query: String,
        page: Int
    ): List<Artist> {
        return tryToCall {
            artistsRemoteDataSource.getArtistsByQuery(query, page).toEntityList()
        }
    }

    override suspend fun getArtistById(id: Long): Artist {
        return tryToCall {
                artistsRemoteDataSource.getArtistById(id).toEntity()
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