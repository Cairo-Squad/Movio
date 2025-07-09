package com.cairosquad.remote.search

import com.cairosquad.remote.BuildConfig
import com.cairosquad.remote.common.utils.callApi
import com.cairosquad.remote.common.utils.constructUrl
import com.cairosquad.repository.search.dataSource.remote.search.RemoteSearchDataSource
import com.cairosquad.repository.search.dataSource.remote.search.dto.ArtistDto
import com.cairosquad.repository.search.dataSource.remote.search.dto.MovieDto
import com.cairosquad.repository.search.dataSource.remote.search.dto.SearchResultDto
import com.cairosquad.repository.search.dataSource.remote.search.dto.SeriesDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RemoteSearchDataSourceImpl(
    private val httpClient: HttpClient
) : RemoteSearchDataSource {
    override suspend fun searchMovies(query: String): List<MovieDto> {
        return callApi<SearchResultDto<MovieDto>> {
            httpClient.get(constructUrl("search/movie")) {
                parameter("query", query)
                parameter("api_key", BuildConfig.API_KEY)
            }
        }.results?.filterNotNull() ?: emptyList()
    }

    override suspend fun searchSeries(query: String): List<SeriesDto> {
        return callApi<SearchResultDto<SeriesDto>> {
            httpClient.get(constructUrl("search/tv")) {
                parameter("query", query)
                parameter("api_key", BuildConfig.API_KEY)
            }
        }.results?.filterNotNull() ?: emptyList()
    }

    override suspend fun searchArtists(query: String): List<ArtistDto> {
        return callApi<SearchResultDto<ArtistDto>> {
            httpClient.get(constructUrl("search/person")) {
                parameter("query", query)
                parameter("api_key", BuildConfig.API_KEY)
            }
        }.results?.filterNotNull() ?: emptyList()
    }

}