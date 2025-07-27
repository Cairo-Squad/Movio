package com.cairosquad.remote.movie

import com.cairosquad.remote.utils.retrofit.safeCallApi
import com.cairosquad.repository.artists.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.movie.data_source.remote.MoviesRemoteDataSource
import com.cairosquad.repository.movie.data_source.remote.dto.GenreDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieDetailsRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.MovieRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import java.time.LocalDate

class MoviesRemoteDataSourceImpl(
    private val apiService: MovieApiService,
) : MoviesRemoteDataSource {

    override suspend fun getMovieById(movieId: Long): MovieDetailsRemoteDto {
        return safeCallApi { apiService.getMovieById(movieId) }
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

    override suspend fun getTopRatingMovies(page: Int, genreId: Long?): List<MovieRemoteDto> {
        return safeCallApi { apiService.getTopRatingMovies(page, genreId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getVideoKey(movieId: Long): String {
        return safeCallApi { apiService.getVideoKey(movieId).getVideoKey() ?: "" }
    }

    override suspend fun getUpcomingMovies(page: Int, genreId: Long?): List<MovieRemoteDto> {
        val today = LocalDate.now()
        val thirtyDaysFromNow = today.plusDays(30)
        return safeCallApi {
            apiService.getUpcomingMovies(
                page, genreId,
                minDate = today.toString(), maxDate = thirtyDaysFromNow.toString()
            )
        }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getNowPlayingMovies(page: Int, genreId: Long?): List<MovieRemoteDto> {
        val today = LocalDate.now()
        val twoWeeksAgo = today.minusWeeks(2)
        return safeCallApi {
            apiService.getNowPlayingMovies(
                page, genreId,
                minDate = twoWeeksAgo.toString(), maxDate = today.toString()
            )
        }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getTrendingMovies(page: Int, genreId: Long?): List<MovieRemoteDto> {
        val today = LocalDate.now()
        val lastMonth = today.minusDays(30)
        return safeCallApi {
            apiService.getTrendingMovies(
                page, genreId,
                minDate = lastMonth.toString(), maxDate = today.toString()
            )
        }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoreRecommendedMovies(
        page: Int,
        genreId: Long?
    ): List<MovieRemoteDto> {
        return safeCallApi { apiService.getMoreRecommendedMovies(page, withGenres = genreId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getFreeToWatchMovies(
        page: Int,
        genreId: Long?
    ): List<MovieRemoteDto> {
        return safeCallApi { apiService.getFreeToWatchMovies(page, genreId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoviesByCategory(
        genreId: Long,
        page: Int
    ): List<MovieRemoteDto> {
        return safeCallApi { apiService.getMoviesByGenre(genreId, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoviesByQuery(query: String, page: Int): List<MovieRemoteDto> {
        return safeCallApi { apiService.getMoviesByQuery(query, page) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getMoviesGenres(): List<GenreDto> {
        return safeCallApi { apiService.getMoviesGenres() }
            .genres?.filterNotNull().orEmpty()
    }

    override suspend fun getPopularMovies(page: Int, genreId: Long?): List<MovieRemoteDto> {
        return safeCallApi { apiService.getPopularMovies(page, genreId) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getAllMovies(
        page: Int,
        genreId: Long?,
        sortBy: String?
    ): List<MovieRemoteDto> {
        return safeCallApi { apiService.getAllMovies(page, genreId, sortBy) }
            .results?.filterNotNull().orEmpty()
    }

    override suspend fun getPersonalizedMovies(page: Int): List<MovieRemoteDto> {
        return safeCallApi { apiService.getPersonalizedMovies(page) }
            .results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }

    override suspend fun getSuggestedMovies(): List<MovieRemoteDto> {
        return safeCallApi { apiService.getSuggestedMovies() }
            .results?.filterNotNull()?.filter { it.id != null } ?: emptyList()
    }
}