package com.cairosquad.remote.movie

import com.cairosquad.remote.utils.retrofit.ApiServiceProvider
import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.movie.data_source.remote.RemoteMovieDataSource
import com.cairosquad.repository.movie.data_source.remote.dto.GenreDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieDetailsRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto
import java.time.LocalDate

class RemoteMovieDataSourceImpl(
    private val apiServiceProvider: ApiServiceProvider,
) : RemoteMovieDataSource {

    override suspend fun getMovie(movieId: Long): MovieDetailsRemoteDto {
        return safeCallApi { apiServiceProvider.getMovieApiService().getMovie(movieId) }
    }

    override suspend fun getMovieReviews(movieId: Long, page: Int): List<ReviewRemoteDto> {
        return safeCallApi { apiServiceProvider.getMovieApiService().getMovieReviews(movieId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getSimilarMovies(movieId: Long, page: Int): List<MovieRemoteDto> {
        return safeCallApi { apiServiceProvider.getMovieApiService().getSimilarMovies(movieId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMovieTopCast(movieId: Long, page: Int): List<ArtistRemoteDto> {
        return safeCallApi { apiServiceProvider.getMovieApiService().getMovieTopCast(movieId, page) }
            .cast?.filterNotNull().orEmpty()
    }

    override suspend fun getTopRatingMovies(page: Int,categoryId: String?): List<MovieRemoteDto> {
        return safeCallApi { apiServiceProvider.getMovieApiService().getTopRatingMovies(page,categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getVideoKey(movieId: Long): String {
        return safeCallApi { apiServiceProvider.getMovieApiService().getVideoKey(movieId).getVideoKey() ?: "" }
    }

    override suspend fun getUpcomingMovies(page: Int,categoryId: String?): List<MovieRemoteDto> {
        val today = LocalDate.now()
        val thirtyDaysFromNow = today.plusDays(30)
        return safeCallApi { apiServiceProvider.getMovieApiService().getUpcomingMovies(page,categoryId,
            minDate = today.toString(), maxDate = thirtyDaysFromNow.toString()
        ) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getNowPlayingMovies(page: Int,categoryId: String?): List<MovieRemoteDto> {
        val today = LocalDate.now()
        val twoWeeksAgo = today.minusWeeks(2)
        return safeCallApi { apiServiceProvider.getMovieApiService().getNowPlayingMovies(page,categoryId,
            minDate = twoWeeksAgo.toString(), maxDate = today.toString()) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getTrendingMovies(page: Int,categoryId: String?): List<MovieRemoteDto> {
        val today = LocalDate.now()
        val lastMonth = today.minusDays(30)
        return safeCallApi { apiServiceProvider.getMovieApiService().getTrendingMovies(page,categoryId,
            minDate = lastMonth.toString(), maxDate = today.toString()
            ) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoreRecommendedMovies(page: Int,categoryId: String?): List<MovieRemoteDto> {
        return safeCallApi { apiServiceProvider.getMovieApiService().getMoreRecommendedMovies(page, withGenres = categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getFreeToWatchMovies(page: Int,categoryId: String?): List<MovieRemoteDto> {
        return safeCallApi { apiServiceProvider.getMovieApiService().getFreeToWatchMovies(page,categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoviesByCategory(
        categoryId: String,
        page: Int
    ): List<MovieRemoteDto> {
        return safeCallApi { apiServiceProvider.getMovieApiService().getMoviesByCategory(categoryId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoviesGenres(): List<GenreDto> {
        return safeCallApi { apiServiceProvider.getMovieApiService().getMoviesGenres() }
            .genres?.filterNotNull().orEmpty()
    }

    override suspend fun getPopularMovies(page: Int,categoryId: String?): List<MovieRemoteDto> {
        return safeCallApi { apiServiceProvider.getMovieApiService().getPopularMovies(page,categoryId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getAllMovies(page: Int,categoryId: String?,sortBy : String?): List<MovieRemoteDto> {
        return safeCallApi { apiServiceProvider.getMovieApiService().getAllMovies(page,categoryId,sortBy) }
            .results?.filterNotNull().orEmpty()
    }
}