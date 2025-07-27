package com.cairosquad.domain.usecase

import com.cairosquad.domain.model.SortType
import com.cairosquad.domain.repository.MoviesRepository
import com.cairosquad.domain.repository.SearchRepository
import com.cairosquad.entity.Artist
import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review

class ManageMoviesUseCase(
    private val moviesRepository: MoviesRepository,
    private val searchRepository: SearchRepository,
) {
    suspend fun getMoviesByQuery(query: String, page: Int ): List<Movie> {
        return moviesRepository.getMoviesByQuery(query,page).also {
            searchRepository.addQuery(query)
        }
    }

    suspend fun getAllMovies(
        page: Int,
        genreId: String? = null,
        sortType: SortType? = null
    ): List<Movie> {
        return moviesRepository.getAllMovies(page, genreId, sortType)
    }

    suspend fun getFreeToWatchMovies(page : Int , categoryId: String? = null): List<Movie> {
        return moviesRepository.getFreeToWatchMovies(page, categoryId)
    }

    suspend fun getMoreRecommendedMovies(page:Int, categoryId: String? = null ) : List<Movie>{
        return moviesRepository.getMoreRecommendedMovies(page,categoryId)
    }

    suspend fun getMovieById(movieId: Long): Movie {
        return moviesRepository.getMovieById(movieId)
    }

    suspend fun getMovieReviews(movieId: Long, page: Int = 1): List<Review> {
        return moviesRepository.getMovieReviews(movieId, page)
    }

    suspend fun getSimilarMovies(movieId: Long, page: Int = 1): List<Movie> {
        return moviesRepository.getSimilarMovies(movieId, page)
    }

    suspend fun getMovieTopCast(movieId: Long, page: Int = 1): List<Artist> {
        return moviesRepository.getMovieTopCast(movieId, page)
    }

    suspend fun getMoviesByCategory( page : Int,categoryId : String): List<Movie> {
        return moviesRepository.getMoviesByCategory(page,categoryId)
    }

    suspend fun getMoviesGenres(): List<Genre>{
        return moviesRepository.getMoviesGenres()
    }

    suspend fun getNowPlayingMovies(page: Int, categoryId : String?  =null ) : List<Movie>{
        return moviesRepository.getNowPlayingMovies(page,categoryId)
    }

    suspend fun getPersonalizedMovies(page : Int): List<Movie> {
        return moviesRepository.getPersonalizedMovies(page)
    }

    suspend fun getPopularMovies(page: Int, categoryId: String? = null) : List<Movie>{
        return moviesRepository.getPopularMovies(page,categoryId)
    }

    suspend fun getSuggestedMovies(): List<Movie> {
        return moviesRepository.getSuggestedMovies()
    }

    suspend fun getTopRatingMovies(page: Int, categoryId: String? = null) : List<Movie>{
        return moviesRepository.getTopRatingMovies(page,categoryId)
    }

    suspend fun getTrendingMovies(page:Int, categoryId: String? = null) : List<Movie>{
        return moviesRepository.getTrendingMovies(page,categoryId)
    }

    suspend fun getUpcomingMovies(page: Int, categoryId: String? = null): List<Movie>{
        return moviesRepository.getUpcomingMovies(page,categoryId)
    }

}