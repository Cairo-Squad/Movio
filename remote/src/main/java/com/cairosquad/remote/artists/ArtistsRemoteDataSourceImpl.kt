package com.cairosquad.remote.artists

import com.cairosquad.remote.BuildConfig
import com.cairosquad.remote.common.utils.callApi
import com.cairosquad.remote.common.utils.constructUrl
import com.cairosquad.repository.artists.data_source.ArtistsRemoteDataSource
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.Serializable

class ArtistsRemoteDataSourceImpl(
    private val httpClient: HttpClient
) : ArtistsRemoteDataSource {
    override suspend fun getArtist(artistId: Long): ArtistRemoteDto {
        return callApi<ArtistRemoteDto> {
            httpClient.get(constructUrl("person/$artistId")) {
                parameter(API_KEY, BuildConfig.API_KEY)
            }
    }
    }

    override suspend fun getMoviesOfArtist(artistId: Long): List<MovieRemoteDto> {
        return callApi<MoviesListResponse> {
            httpClient.get(constructUrl("person/$artistId/movie_credits")) {
                parameter(API_KEY, BuildConfig.API_KEY)
            }
        }.cast.filter { it.id != null }
    }

    override suspend fun getSeriesOfArtist(artistId: Long): List<SeriesRemoteDto> {
        return callApi<SeriesListResponse> {
            httpClient.get(constructUrl("person/$artistId/tv_credits")) {
                parameter(API_KEY, BuildConfig.API_KEY)
            }
        }.cast.filter { it.id != null }
    }

    @Serializable
    private data class MoviesListResponse(
        val cast: List<MovieRemoteDto>
    )

    @Serializable
    private data class SeriesListResponse(
        val cast: List<SeriesRemoteDto>
    )

    companion object {
        private const val API_KEY = "api_key"
    }
}