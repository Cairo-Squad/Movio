package com.cairosquad.remote.search

import com.cairosquad.remote.BuildConfig
import com.cairosquad.remote.common.utils.callApi
import com.cairosquad.remote.common.utils.constructUrl
import com.cairosquad.repository.search.data_source.remote.RemoteSearchDataSource
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ResultResponse
import com.cairosquad.repository.search.data_source.remote.dto.SeriesRemoteDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RemoteSearchDataSourceImpl(
    private val httpClient: HttpClient
) : RemoteSearchDataSource {
    override suspend fun getMovies(query: String): List<MovieRemoteDto> {
        return callApi<ResultResponse<MovieRemoteDto>> {
            httpClient.get(constructUrl("search/movie")) {
                parameter(QUERY, query)
                parameter(API_KEY, BuildConfig.API_KEY)
            }
        }.results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }

    override suspend fun getSeries(query: String): List<SeriesRemoteDto> {
        return callApi<ResultResponse<SeriesRemoteDto>> {
            httpClient.get(constructUrl("search/tv")) {
                parameter(QUERY, query)
                parameter(API_KEY, BuildConfig.API_KEY)
            }
        }.results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }

    override suspend fun getArtists(query: String): List<ArtistRemoteDto> {
        return callApi<ResultResponse<ArtistRemoteDto>> {
            httpClient.get(constructUrl("search/person")) {
                parameter(QUERY, query)
                parameter(API_KEY, BuildConfig.API_KEY)
            }
        }.results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }

    companion object {
        private const val QUERY = "query"
        private const val API_KEY = "api_key"
    }

}