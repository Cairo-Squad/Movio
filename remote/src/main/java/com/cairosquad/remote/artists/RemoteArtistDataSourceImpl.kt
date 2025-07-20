package com.cairosquad.remote.artists

import com.cairosquad.repository.artists.data_source.ArtistsRemoteDataSource
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto

class RemoteArtistDataSourceImpl(
    private val apiService: ArtistsApiService
) : ArtistsRemoteDataSource {

    override suspend fun getArtist(artistId: Long): ArtistRemoteDto {
        return apiService.getArtist(artistId)
    }

    override suspend fun getMoviesOfArtist(artistId: Long): List<MovieRemoteDto> {
        return apiService.getMoviesOfArtist(artistId).cast.filter { it.id != null }
    }

    override suspend fun getSeriesOfArtist(artistId: Long): List<SeriesRemoteDto> {
        return apiService.getSeriesOfArtist(artistId).cast.filter { it.id != null }
    }
}