package com.cairosquad.viewmodel.searchviewmodel

import com.cairosquad.entity.Artist
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.search.SearchScreenState
import com.cairosquad.viewmodel.search.toUiState
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SearchUiStateMapperTest {

    @Test
    fun `should map Movie to MovieUiState when toUiState is called`() {
        // When
        val uiState = movie.toUiState()

        // Then
        assertThat(uiState).isEqualTo(
            SearchScreenState.MovieUiState(
                id = movie.id,
                title = movie.title,
                rating = movie.rating.roundToFirstDecimalPlace(),
                posterPath = movie.posterPath
            )
        )
    }

    @Test
    fun `should map Artist to ArtistUiState when toUiState is called`() {
        // When
        val uiState = artist.toUiState()

        // Then
        assertThat(uiState).isEqualTo(
            SearchScreenState.ArtistUiState(
                id = artist.id,
                name = artist.name,
                photoPath = artist.photoPath
            )
        )
    }

    @Test
    fun `should map Series to SeriesUiState when toUiState is called`() {
        // When
        val uiState = series.toUiState()

        // Then
        assertThat(uiState).isEqualTo(
            SearchScreenState.SeriesUiState(
                id = series.id,
                title = series.title,
                rating = series.rating.roundToFirstDecimalPlace(),
                posterPath = series.posterPath
            )
        )
    }

    private companion object {
        val movie = Movie(
            id = 101L,
            title = "Inception",
            posterPath = "/inc.jpg",
            rating = 8.0f,
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            runtimeMinutes = 5,
            trailerPath = ""
        )

        val series = Series(
            id = 202L,
            title = "Breaking Bad",
            posterPath = "/bb.jpg",
            rating = 9.0f,
            trailerPath = "",
            genres = emptyList(),
            overview = "",
            releaseDate = 0L,
            seasonsCount = 0
        )

        val artist = Artist(
            id = 303L,
            name = "Emma Watson",
            photoPath = "/emma.jpg",
            country = "",
            birthDate = 0L,
            biography = "",
            department = ""
        )
    }

}