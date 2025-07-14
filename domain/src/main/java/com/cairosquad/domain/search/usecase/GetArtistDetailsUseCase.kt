package com.cairosquad.domain.search.usecase

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series

class GetArtistDetailsUseCase {

    suspend fun getArtistById(artistId: Long): Artist {
        return fakeArtist
    }

    suspend fun getMoviesThatArtistIsKnownFor(artistId: Long): List<Movie> {
        return listOf(fakeMovie)
    }

    suspend fun getSeriesThatArtistIsKnownFor(artistId: Long): List<Series> {
        return listOf(fakeSeries)
    }

    private companion object {

        val fakeMovie = Movie(id = 1, title = "Ballerina", rating = 0f, posterPath = "", genres = emptyList())

        val fakeArtist = Artist(id = 1, name = "", photoPath = "")

        val fakeSeries = Series(id = 1, title = "", rating = 0f, posterPath = "", genres = emptyList())
    }
}