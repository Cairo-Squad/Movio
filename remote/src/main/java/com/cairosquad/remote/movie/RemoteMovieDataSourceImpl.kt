package com.cairosquad.remote.movie

import com.cairosquad.remote.BuildConfig
import com.cairosquad.remote.common.utils.callApi
import com.cairosquad.remote.common.utils.constructUrl
import com.cairosquad.repository.movie.data_source.remote.RemoteMovieDataSource
import com.cairosquad.repository.movie.data_source.remote.dto.CastResponse
import com.cairosquad.repository.movie.data_source.remote.dto.MovieDetailsRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ResultResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RemoteMovieDataSourceImpl(
    private val httpClient: HttpClient
) : RemoteMovieDataSource {
    override suspend fun getMovie(movieId: Long): MovieDetailsRemoteDto {
        return callApi<MovieDetailsRemoteDto> {
            httpClient.get(constructUrl("movie/$movieId")) {
                parameter(API_KEY, BuildConfig.API_KEY)
            }
        }
    }

    override suspend fun getMovieReviews(movieId: Long, page: Int): List<ReviewRemoteDto> {
        return callApi<ResultResponse<ReviewRemoteDto>> {
            httpClient.get(constructUrl("movie/$movieId/reviews")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
            }
        }.results?.filterNotNull().orEmpty()
    }

    override suspend fun getSimilarMovies(movieId: Long, page: Int): List<MovieRemoteDto> {
        return callApi<ResultResponse<MovieRemoteDto>> {
            httpClient.get(constructUrl("movie/$movieId/similar")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
            }
        }.results?.filterNotNull().orEmpty()
    }

    override suspend fun getMovieTopCast(movieId: Long, page: Int): List<ArtistRemoteDto> {
        return callApi<CastResponse> {
            httpClient.get(constructUrl("movie/$movieId/credits")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
            }
        }.cast?.filterNotNull().orEmpty()
    }

    companion object {
        private const val API_KEY = "api_key"
        private const val PAGE = "page"
    }
}