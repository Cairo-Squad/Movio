package com.cairosquad.repository.artists.data_source.remote

import com.cairosquad.repository.artists.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto

interface ArtistsRemoteDataSource {
    suspend fun getArtistsByQuery(query: String,page:Int): List<ArtistRemoteDto>
    suspend fun getArtist(artistId: Long): ArtistRemoteDto
    suspend fun getMoviesOfArtist(artistId: Long): List<MovieRemoteDto>
    suspend fun getSeriesOfArtist(artistId: Long): List<SeriesRemoteDto>
}