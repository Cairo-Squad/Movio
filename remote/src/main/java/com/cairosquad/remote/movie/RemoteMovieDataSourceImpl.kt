package com.cairosquad.remote.movie

import com.cairosquad.remote.BuildConfig
import com.cairosquad.remote.series.RemoteSeriesDataSourceImpl
import com.cairosquad.remote.utils.callApi
import com.cairosquad.remote.utils.constructUrl
import com.cairosquad.repository.movie.data_source.remote.RemoteMovieDataSource
import com.cairosquad.repository.movie.data_source.remote.dto.CreditResponse
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
        return callApi<CreditResponse> {
            httpClient.get(constructUrl("movie/$movieId/credits")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
            }
        }.cast?.filterNotNull().orEmpty()
    }

    override suspend fun getTopRatingMovies(page: Int): List<MovieRemoteDto> {
        return callApi<ResultResponse<MovieRemoteDto>> {
            httpClient.get(constructUrl("movie/top_rated")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
            }
        }.results?.filterNotNull().orEmpty()
    }

    override suspend fun getUpcomingMovies(page: Int): List<MovieRemoteDto> {
        return callApi<ResultResponse<MovieRemoteDto>> {
            httpClient.get(constructUrl("movie/upcoming")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
            }
        }.results?.filterNotNull().orEmpty()
    }

    override suspend fun getNowPlayingMovies(page: Int): List<MovieRemoteDto> {
        return callApi<ResultResponse<MovieRemoteDto>> {
            httpClient.get(constructUrl("movie/now_playing")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
            }
        }.results?.filterNotNull().orEmpty()
    }

    override suspend fun getTrendingMovies(page: Int): List<MovieRemoteDto> {
        return callApi<ResultResponse<MovieRemoteDto>> {
            httpClient.get(constructUrl("trending/movie/{day}")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
            }
        }.results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoreRecommendedMovies(page: Int): List<MovieRemoteDto> {

        return callApi<ResultResponse<MovieRemoteDto>> {
            httpClient.get(constructUrl("movie/popular")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
            }
        }.results?.filterNotNull().orEmpty()
    }

    override suspend fun getFreeToWatchMovies(page: Int): List<MovieRemoteDto> {
        return callApi<ResultResponse<MovieRemoteDto>> {
            httpClient.get(constructUrl("discover/movie")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
                parameter(WITH_WATCH_PROVIDERS,"free")
            }
        }.results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoviesByCategory(
        categoryId: String,
        page: Int
    ): List<MovieRemoteDto> {
        return callApi<ResultResponse<MovieRemoteDto>> {
            httpClient.get(constructUrl("discover/movie")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
                parameter(WITH_GENRES, categoryId)
            }
        }.results?.filterNotNull().orEmpty()
    }

    override suspend fun getRandomMoviesUseCase(page: Int): List<MovieRemoteDto> {
        return callApi<ResultResponse<MovieRemoteDto>> {
            httpClient.get(constructUrl("discover/movie")) {
                parameter(API_KEY, BuildConfig.API_KEY)
                parameter(PAGE, page)
            }
        }.results?.filterNotNull().orEmpty()
    }

    companion object {
        private const val API_KEY = "api_key"
        private const val PAGE = "page"
        private const val WITH_WATCH_PROVIDERS = "with_watch_providers"
        private const val WITH_GENRES = "with_genres"
    }
}