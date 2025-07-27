package com.cairosquad.repository.artists.data_source.remote

import com.cairosquad.repository.artists.data_source.remote.dto.ArtistRemoteDto

interface ArtistsRemoteDataSource {
    suspend fun getArtistsByQuery(query: String,page:Int): List<ArtistRemoteDto>
    suspend fun getArtistById(id: Long): ArtistRemoteDto
    suspend fun getMovieTopCast(movieId: Long, page: Int): List<ArtistRemoteDto>
    suspend fun getSeriesTopCast(seriesId: Long, page: Int): List<ArtistRemoteDto>
}