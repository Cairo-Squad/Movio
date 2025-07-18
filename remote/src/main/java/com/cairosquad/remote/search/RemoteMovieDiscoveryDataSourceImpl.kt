package com.cairosquad.remote.search

import com.cairosquad.remote.BuildConfig
import com.cairosquad.remote.utils.callApi
import com.cairosquad.remote.utils.constructUrl
import com.cairosquad.repository.search.data_source.remote.RemoteMovieDiscoveryDataSource
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.SearchResultResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RemoteMovieDiscoveryDataSourceImpl(
    private val httpClient: HttpClient
) : RemoteMovieDiscoveryDataSource {
    override suspend fun getPersonalizedMovies(page : Int): List<MovieRemoteDto> {
        return callApi<SearchResultResponse<MovieRemoteDto>> {
            httpClient.get(constructUrl("movie/top_rated")) {
                parameter(PAGE_NUMBER, page)
                parameter(API_KEY, BuildConfig.API_KEY)
            }
        }.results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }

    override suspend fun getSuggestedMovies(): List<MovieRemoteDto> {
        return callApi<SearchResultResponse<MovieRemoteDto>> {
            httpClient.get(constructUrl("movie/now_playing")) {
                parameter(API_KEY, BuildConfig.API_KEY)
            }
        }.results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }
    companion object {
        private const val API_KEY = "api_key"
        private const val PAGE_NUMBER = "page"
    }
}