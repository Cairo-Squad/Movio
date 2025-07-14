package com.cairosquad.domain.search.usecase

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Review

class GetMoviesDetailsUseCase {

    suspend fun getMovieById(movieId: Long): Movie {
        return fakeMovie
    }

    suspend fun getReviewsByMovieId(movieId: Long): List<Review> {
        return listOf(fakeReview)
    }

    suspend fun getSimilarMovies(movieId: Long): List<Movie> {
        return listOf(fakeMovie)
    }

    suspend fun getTopCastByMovieId(movieId: Long): List<Artist> {
        return listOf(fakeArtist)
    }

    private companion object {
        val fakeMovie = Movie(id = 1, title = "Ballerina", rating = 0f, posterPath = "", genres = emptyList())

        val fakeArtist = Artist(id = 1, name = "", photoPath = "")

        val fakeReview = Review(id = 1, author = "", authorPhotoPath = "", rating = "", date = 0, description = "")
    }
}