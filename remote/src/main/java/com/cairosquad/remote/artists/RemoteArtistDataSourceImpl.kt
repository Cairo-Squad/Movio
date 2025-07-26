package com.cairosquad.remote.artists

import com.cairosquad.remote.utils.retrofit.ApiServiceProvider
import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.artists.data_source.ArtistsRemoteDataSource
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto

class RemoteArtistDataSourceImpl(
    private val apiServiceProvider: ApiServiceProvider
) : ArtistsRemoteDataSource {

    override suspend fun getArtist(artistId: Long): ArtistRemoteDto {
        return safeCallApi { apiServiceProvider.getArtistsApiService().getArtist(artistId) }
    }

    override suspend fun getMoviesOfArtist(artistId: Long): List<MovieRemoteDto> {
        return safeCallApi { apiServiceProvider.getArtistsApiService().getMoviesOfArtist(artistId) }
            .movies.filter { it.id != null }
    }

    override suspend fun getSeriesOfArtist(artistId: Long): List<SeriesRemoteDto> {
        return safeCallApi { apiServiceProvider.getArtistsApiService().getSeriesOfArtist(artistId) }
            .series.filter { it.id != null }
    }
}