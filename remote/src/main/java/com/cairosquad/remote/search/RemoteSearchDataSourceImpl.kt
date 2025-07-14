package com.cairosquad.remote.search

import com.cairosquad.remote.BuildConfig
import com.cairosquad.remote.common.utils.callApi
import com.cairosquad.remote.common.utils.constructUrl
import com.cairosquad.repository.search.data_source.remote.RemoteSearchDataSource
import com.cairosquad.repository.search.data_source.remote.dto.ApiArtistDto
import com.cairosquad.repository.search.data_source.remote.dto.ApiMovieDto
import com.cairosquad.repository.search.data_source.remote.dto.SearchResultResponse
import com.cairosquad.repository.search.data_source.remote.dto.ApiSeriesDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RemoteSearchDataSourceImpl(
    private val httpClient: HttpClient
) : RemoteSearchDataSource {
    override suspend fun getMovies(query: String): List<ApiMovieDto> {
        return callApi<SearchResultResponse<ApiMovieDto>> {
            httpClient.get(constructUrl("search/movie")) {
                parameter(QUERY, query)
                parameter(API_KEY, BuildConfig.API_KEY)
            }
        }.results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }

    override suspend fun getSeries(query: String): List<ApiSeriesDto> {
        return callApi<SearchResultResponse<ApiSeriesDto>> {
            httpClient.get(constructUrl("search/tv")) {
                parameter(QUERY, query)
                parameter(API_KEY, BuildConfig.API_KEY)
            }
        }.results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }

    override suspend fun getArtists(query: String): List<ApiArtistDto> {
        return callApi<SearchResultResponse<ApiArtistDto>> {
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