package com.cairosquad.viewmodel.artistviewmodel

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.details.artist.ArtistScreenState
import com.cairosquad.viewmodel.details.artist.toArtistMovieUiState
import com.cairosquad.viewmodel.details.artist.toArtistSeriesUiState
import com.cairosquad.viewmodel.details.artist.toArtistUiState
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ArtistUiStateMapperTest {

    @Test
    fun `should map Artist to ArtistUiState when toArtistUiState is called`() {
        // When
        val uiState = artist.toArtistUiState()

        // Then
        assertThat(uiState).isEqualTo(
            ArtistScreenState.ArtistUiState(
                id = artist.id,
                name = artist.name,
                photoPath = artist.photoPath,
                country = artist.country,
                birthDate = artist.birthDate,
                biography = artist.biography,
                department = artist.department
            )
        )
    }

    @Test
    fun `should map Movie to MovieUiState when toArtistMovieUiState is called`() {
        // When
        val uiState = movie.toArtistMovieUiState()

        // Then
        assertThat(uiState).isEqualTo(
            ArtistScreenState.MovieUiState(
                id = movie.id,
                title = movie.title,
                rating = movie.rating,
                posterPath = movie.posterPath
            )
        )
    }

    @Test
    fun `should map Series to SeriesUiState when toArtistSeriesUiState is called`() {
        // When
        val uiState = series.toArtistSeriesUiState()

        // Then
        assertThat(uiState).isEqualTo(
            ArtistScreenState.SeriesUiState(
                id = series.id,
                title = series.title,
                rating = series.rating,
                posterPath = series.posterPath
            )
        )
    }

    private companion object {
        val artist = Artist(
            id = 101L,
            name = "Emma Watson",
            photoPath = "/emma.jpg",
            country = "United Kingdom",
            birthDate = 19900415,
            biography = "British actress and activist",
            department = "Acting"
        )

        val movie = Movie(
            id = 202L,
            title = "Harry Potter",
            posterPath = "/hp.jpg",
            rating = 8.5f
        )

        val series = Series(
            id = 303L,
            title = "Game of Thrones",
            posterPath = "/got.jpg",
            rating = 9.0f
        )
    }
}