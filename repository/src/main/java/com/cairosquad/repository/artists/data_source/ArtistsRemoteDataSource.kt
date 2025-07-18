package com.cairosquad.repository.artists.data_source

import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto

interface ArtistsRemoteDataSource {
    suspend fun getArtist(artistId: Long): ArtistRemoteDto
    suspend fun getMoviesOfArtist(artistId: Long): List<MovieRemoteDto>
    suspend fun getSeriesOfArtist(artistId: Long): List<SeriesRemoteDto>
}