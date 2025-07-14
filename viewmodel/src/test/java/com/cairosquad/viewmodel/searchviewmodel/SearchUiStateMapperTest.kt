package com.cairosquad.viewmodel.searchviewmodel

import com.cairosquad.entity.*
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UiStateMapperTest {

    @Test
    fun `Movie maps to MovieScreenState correctly`() {
        val uiState = movie.toUiState()

        assertThat(uiState).isEqualTo(
            SearchScreenState.MovieUiState(
                id = movie.id,
                title = movie.title,
                rating = movie.rating / 2,
                posterPath = movie.posterPath
            )
        )
    }

    @Test
    fun `Artist maps to ArtistScreenState correctly`() {
        val uiState = artist.toUiState()

        assertThat(uiState).isEqualTo(
            SearchScreenState.ArtistUiState(
                id = artist.id,
                name = artist.name,
                photoPath = artist.photoPath
            )
        )
    }

    @Test
    fun `Series maps to SeriesScreenState correctly`() {
        val uiState = series.toUiState()

        assertThat(uiState).isEqualTo(
            SearchScreenState.SeriesUiState(
                id = series.id,
                title = series.title,
                rating = series.rating / 2,
                posterPath = series.posterPath
            )
        )
    }

    private companion object {
        val movie = Movie(
            id = 101L,
            title = "Inception",
            posterPath = "/inc.jpg",
            rating = 8.0f
        )

        val series = Series(
            id = 202L,
            title = "Breaking Bad",
            posterPath = "/bb.jpg",
            rating = 9.0f
        )

        val artist = Artist(
            id = 303L,
            name = "Emma Watson",
            photoPath = "/emma.jpg"
        )
    }
}

