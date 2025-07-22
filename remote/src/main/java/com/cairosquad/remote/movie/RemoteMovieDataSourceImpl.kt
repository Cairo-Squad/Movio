package com.cairosquad.remote.movie

import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.movie.data_source.remote.RemoteMovieDataSource
import com.cairosquad.repository.movie.data_source.remote.dto.GenreDto
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

    override suspend fun getTopRatingMovies(page: Int,categoryId: String?): List<MovieRemoteDto> {
        return safeCallApi { apiService.getTopRatingMovies(page,categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getUpcomingMovies(page: Int,categoryId: String?): List<MovieRemoteDto> {
        return safeCallApi { apiService.getUpcomingMovies(page,categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getNowPlayingMovies(page: Int,categoryId: String?): List<MovieRemoteDto> {
        return safeCallApi { apiService.getNowPlayingMovies(page,categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getTrendingMovies(page: Int,categoryId: String?): List<MovieRemoteDto> {
        return safeCallApi { apiService.getTrendingMovies(page,categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoreRecommendedMovies(page: Int,categoryId: String?): List<MovieRemoteDto> {
        return safeCallApi { apiService.getMoreRecommendedMovies(page, withGenres = categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getFreeToWatchMovies(page: Int,categoryId: String?): List<MovieRemoteDto> {
        return safeCallApi { apiService.getFreeToWatchMovies(page,categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoviesByCategory(
        categoryId: String,
        page: Int
    ): List<MovieRemoteDto> {
        return safeCallApi { apiService.getMoviesByCategory(categoryId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoviesGenres(): List<GenreDto> {
        return safeCallApi { apiService.getMoviesGenres() }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getPopularMovies(page: Int,categoryId: String?): List<MovieRemoteDto> {
        return safeCallApi { apiService.getPopularMovies(page,categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getAllMovies(page: Int,categoryId: String?): List<MovieRemoteDto> {
        return safeCallApi { apiService.getAllMovies(page,categoryId) }
            .results?.filterNotNull().orEmpty()
    }


}