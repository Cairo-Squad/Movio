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
    }

    override suspend fun getTopRatingMovies(page: Int): List<MovieRemoteDto> {
        return safeCallApi { apiService.getTopRatingMovies(page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getUpcomingMovies(page: Int): List<MovieRemoteDto> {
        return safeCallApi { apiService.getUpcomingMovies(page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getNowPlayingMovies(page: Int): List<MovieRemoteDto> {
        return safeCallApi { apiService.getNowPlayingMovies(page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getTrendingMovies(page: Int): List<MovieRemoteDto> {
        return safeCallApi { apiService.getTrendingMovies(page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoreRecommendedMovies(page: Int): List<MovieRemoteDto> {
        return safeCallApi { apiService.getMoreRecommendedMovies(page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getFreeToWatchMovies(page: Int): List<MovieRemoteDto> {
        return safeCallApi { apiService.getFreeToWatchMovies(page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoviesByCategory(
        categoryId: String,
        page: Int
    ): List<MovieRemoteDto> {
        return safeCallApi { apiService.getMoviesByCategory(categoryId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoviesGenres(page: Int): List<MovieRemoteDto> {
        return safeCallApi { apiService.getMoviesGenres(page) }
            .results?.filterNotNull().orEmpty()
    }
}