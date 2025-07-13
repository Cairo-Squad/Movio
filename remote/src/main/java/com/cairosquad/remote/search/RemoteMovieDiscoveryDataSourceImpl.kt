package com.cairosquad.remote.search

import com.cairosquad.remote.BuildConfig
import com.cairosquad.remote.common.utils.callApi
import com.cairosquad.remote.common.utils.constructUrl
import com.cairosquad.repository.search.data_source.remote.RemoteMovieDiscoveryDataSource
import com.cairosquad.repository.search.data_source.remote.dto.MovieDto
import com.cairosquad.repository.search.data_source.remote.dto.SearchResultDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RemoteMovieDiscoveryDataSourceImpl(
    private val httpClient: HttpClient
) : RemoteMovieDiscoveryDataSource {
    override suspend fun getPersonalizedMovies(): List<MovieDto> {
        return callApi<SearchResultDto<MovieDto>> {
            httpClient.get(constructUrl("movie/top_rated")) {
                parameter(API_KEY, BuildConfig.API_KEY)
            }
        }.results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }

    override suspend fun getSuggestedMovies(): List<MovieDto> {
        return callApi<SearchResultDto<MovieDto>> {
            httpClient.get(constructUrl("movie/now_playing")) {
                parameter(API_KEY, BuildConfig.API_KEY)
            }
        }.results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }

    companion object {
        private const val API_KEY = "api_key"
    }
}