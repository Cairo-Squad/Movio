package com.cairosquad.remote.artists

import com.cairosquad.remote.BuildConfig
import com.cairosquad.repository.artists.data_source.ArtistsRemoteDataSource
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto
import java.util.Locale

class RemoteArtistDataSourceImpl(
    private val apiService: ArtistsApiService
) : ArtistsRemoteDataSource {

    private val apiKey = BuildConfig.API_KEY
    private val language = Locale.getDefault().toLanguageTag()

    override suspend fun getArtist(artistId: Long): ArtistRemoteDto {
        return apiService.getArtist(artistId, apiKey, language)
    }

    override suspend fun getMoviesOfArtist(artistId: Long): List<MovieRemoteDto> {
        return apiService.getMoviesOfArtist(artistId, apiKey, language).cast.filter { it.id != null }
    }

    override suspend fun getSeriesOfArtist(artistId: Long): List<SeriesRemoteDto> {
        return apiService.getSeriesOfArtist(artistId, apiKey, language).cast.filter { it.id != null }
    }
}