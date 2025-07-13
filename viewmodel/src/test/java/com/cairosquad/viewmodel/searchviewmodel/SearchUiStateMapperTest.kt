package com.cairosquad.viewmodel.searchviewmodel

import com.cairosquad.entity.*
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SearchUiStateMapperTest {

    @Test
    fun `Movie maps to MovieUiState correctly`() {
        val movie = Movie(
            id = 101L,
            title = "Inception",
            rating = 8.0f,
            posterPath = "/inception.jpg"
        )

        val uiState = movie.toUiState()

        assertThat(uiState).isEqualTo(
            SearchUiState.MovieUiState(
                id = 101L,
                title = "Inception",
                rating = 4.0f,
                posterPath = "/inception.jpg"
            )
        )
    }

    @Test
    fun `Artist maps to ArtistUiState correctly`() {
        val artist = Artist(
            id = 202L,
            name = "Emma Watson",
            photoPath = "/emma.jpg"
        )

        val uiState = artist.toUiState()

        assertThat(uiState).isEqualTo(
            SearchUiState.ArtistUiState(
                id = 202L,
                name = "Emma Watson",
                photoPath = "/emma.jpg"
            )
        )
    }

    @Test
    fun `Series maps to SeriesUiState correctly`() {
        val series = Series(
            id = 303L,
            title = "Breaking Bad",
            rating = 9.0f,
            posterPath = "/bb.jpg"
        )

        val uiState = series.toUiState()

        assertThat(uiState).isEqualTo(
            SearchUiState.SeriesUiState(
                id = 303L,
                title = "Breaking Bad",
                rating = 4.5f,
                posterPath = "/bb.jpg"
            )
        )
    }
}
