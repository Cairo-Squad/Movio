package com.cairosquad.domain.usecase

import com.cairosquad.domain.model.SortType
import com.cairosquad.domain.repository.ArtistsRepository
import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review
import javax.inject.Inject

class ManageMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val searchRepository: SearchRepository,
    private val artistRepository: ArtistsRepository
) {
    suspend fun getMoviesByQuery(query: String, page: Int): List<Movie> {
        return moviesRepository.getMoviesByQuery(query, page).also {
            searchRepository.addQuery(query)
        }
    }

    suspend fun getAllMovies(
        page: Int,
        genreId: Long? = null,
        sortType: SortType? = null
    ): List<Movie> {
        return moviesRepository.getAllMovies(page, genreId, sortType)
    }

    suspend fun getFreeToWatchMovies(page: Int, genreId: Long? = null): List<Movie> {
        return moviesRepository.getFreeToWatchMovies(page, genreId)
    }

    suspend fun getMoreRecommendedMovies(page: Int, genreId: Long? = null): List<Movie> {
        return moviesRepository.getMoreRecommendedMovies(page, genreId)
    }

    suspend fun getMovieById(id: Long): Movie {
        return moviesRepository.getMovieById(id)
    }

    suspend fun getMovieReviews(movieId: Long, page: Int = 1): List<Review> {
        return moviesRepository.getMovieReviews(movieId, page)
    }

    suspend fun getSimilarMovies(movieId: Long, page: Int = 1): List<Movie> {
        return moviesRepository.getSimilarMovies(movieId, page)
    }

    suspend fun getMovieTopCast(movieId: Long, page: Int = 1): List<Artist> {
        return artistRepository.getMovieTopCast(movieId, page)
    }

    suspend fun getMoviesByCategory(page: Int, genreId: Long): List<Movie> {
        return moviesRepository.getMoviesByCategory(page, genreId)
    }

    suspend fun getMoviesGenres(): List<Genre> {
        return moviesRepository.getMoviesGenres()
    }

    suspend fun getNowPlayingMovies(page: Int, genreId: Long? = null): List<Movie> {
        return moviesRepository.getNowPlayingMovies(page, genreId)
    }

    suspend fun getPersonalizedMovies(page: Int): List<Movie> {
        return moviesRepository.getPersonalizedMovies(page)
    }

    suspend fun getPopularMovies(page: Int, genreId: Long? = null): List<Movie> {
        return moviesRepository.getPopularMovies(page, genreId)
    }

    suspend fun getSuggestedMovies(): List<Movie> {
        return moviesRepository.getSuggestedMovies()
    }

    suspend fun getTopRatingMovies(page: Int, genreId: Long? = null): List<Movie> {
        return moviesRepository.getTopRatingMovies(page, genreId)
    }

    suspend fun getTrendingMovies(page: Int, genreId: Long? = null): List<Movie> {
        return moviesRepository.getTrendingMovies(page, genreId)
    }

    suspend fun getUpcomingMovies(page: Int, genreId: Long? = null): List<Movie> {
        return moviesRepository.getUpcomingMovies(page, genreId)
    }

}