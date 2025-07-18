package com.cairosquad.repository.movie.data_source.remote

import com.cairosquad.repository.movie.data_source.remote.dto.MovieDetailsRemoteDto
import com.cairosquad.repository.movie.data_source.remote.dto.ReviewRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.ArtistRemoteDto
import com.cairosquad.repository.search.data_source.remote.dto.MovieRemoteDto


interface MovieDataSource {
    suspend fun getMovie(movieId: Long): MovieDetailsRemoteDto

    suspend fun getMovieReviews(movieId: Long): List<ReviewRemoteDto>

    suspend fun getSimilarMovies(movieId: Long): List<MovieRemoteDto>

    suspend fun getMovieTopCast(movieId: Long): List<ArtistRemoteDto>

}