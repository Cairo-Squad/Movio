package com.cairosquad.remote.movie

import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.movie.data_source.remote.RemoteMovieDataSource
import com.cairosquad.repository.movie.data_source.remote.dto.MovieDetailsRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto

class RemoteMovieDataSourceImpl(
    private val apiService: MovieApiService,
) : RemoteMovieDataSource {

    override suspend fun getMovie(movieId: Long): MovieDetailsRemoteDto {
        return safeCallApi { apiService.getMovie(movieId) }
    }

    override suspend fun getMovieReviews(movieId: Long, page: Int): List<ReviewRemoteDto> {
        return safeCallApi { apiService.getMovieReviews(movieId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSimilarMovies(movieId: Long, page: Int): List<MovieRemoteDto> {
        return safeCallApi { apiService.getSimilarMovies(movieId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMovieTopCast(movieId: Long, page: Int): List<ArtistRemoteDto> {
        return safeCallApi { apiService.getMovieTopCast(movieId, page) }
            .cast?.filterNotNull().orEmpty()

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