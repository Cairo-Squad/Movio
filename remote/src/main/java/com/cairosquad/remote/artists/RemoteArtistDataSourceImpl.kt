package com.cairosquad.remote.artists

import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.artists.data_source.remote.ArtistsRemoteDataSource
import com.cairosquad.repository.artists.data_source.remote.dto.ArtistRemoteDto

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

    override suspend fun getArtistById(id: Long): ArtistRemoteDto {
        return safeCallApi { apiService.getArtistById(id) }
    }

    override suspend fun getMovieTopCast(movieId: Long, page: Int): List<ArtistRemoteDto> {
        return safeCallApi { apiService.getMovieTopCast(movieId, page) }
            .cast?.filterNotNull().orEmpty()
    }

    override suspend fun getSeriesTopCast(
        seriesId: Long,
        page: Int
    ): List<ArtistRemoteDto> {
        return safeCallApi { apiService.getSeriesTopCast(seriesId, page) }
            .cast?.filterNotNull().orEmpty()
    }
}