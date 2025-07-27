package com.cairosquad.remote.artists

import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.artists.data_source.remote.ArtistsRemoteDataSource
import com.cairosquad.repository.artists.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.series.data_source.remote.dto.SeriesRemoteDto

class RemoteArtistDataSourceImpl(
    private val apiService: ArtistsApiService
) : ArtistsRemoteDataSource {
    override suspend fun getArtistsByQuery(
        query: String,
        page: Int
    ): List<ArtistRemoteDto> {
        return safeCallApi { apiService.getArtistsByQuery(query, page) }
            .results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }

    override suspend fun getArtist(artistId: Long): ArtistRemoteDto {
        return safeCallApi { apiService.getArtist(artistId) }
    }

    override suspend fun getMoviesOfArtist(artistId: Long): List<MovieRemoteDto> {
        return safeCallApi { apiService.getMoviesOfArtist(artistId) }
            .movies.filter { it.id != null }
    }

    override suspend fun getSeriesOfArtist(artistId: Long): List<SeriesRemoteDto> {
        return safeCallApi { apiService.getSeriesOfArtist(artistId) }
            .series.filter { it.id != null }
    }
}