package com.cairosquad.viewmodel.library.view_all_history

import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.util.TimeUtil
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MapperTest {

    @Test
    fun `Series toUiState should map all fields correctly`() {
        // Arrange
        val series = Series(
            id = 101,
            title = "Breaking Bad",
            rating = 9.5f,
            posterPath = "/poster.jpg",
            genres = listOf(Genre(1, "Drama"), Genre(2, "Crime")),
            seasonsCount = 5,
            releaseDate = 1625097600000, // Example timestamp
            overview = "A chemistry teacher turns to crime.",
            trailerPath = "/trailer.mp4"
        )

        // Act
        val uiState = series.toUiState()

        // Assert
        assertThat(uiState.id).isEqualTo(series.id)
        assertThat(uiState.title).isEqualTo(series.title)
        assertThat(uiState.rating).isEqualTo(series.rating)
        assertThat(uiState.posterPath).isEqualTo(series.posterPath)
        assertThat(uiState.genres).containsExactly("Drama", "Crime")
        assertThat(uiState.seasonsCount).isEqualTo(series.seasonsCount)
        assertThat(uiState.releaseDate)
            .isEqualTo(TimeUtil.convertLongToNamedDate(series.releaseDate))
        assertThat(uiState.overview).isEqualTo(series.overview)
        assertThat(uiState.trailerPath).isEqualTo(series.trailerPath)
    }

    @Test
    fun `Movie toUiState should map all fields correctly`() {
        // Arrange
        val movie = Movie(
            id = 202,
            title = "Inception",
            rating = 8.8f,
            posterPath = "/inception.jpg",
            genres = listOf(Genre(1, "Sci-Fi"), Genre(2, "Action")),
            overview = "A thief who steals corporate secrets through dream-sharing.",
            releaseDate = 1279324800000, // Example timestamp
            runtimeMinutes = 148,
            trailerPath = "/inception_trailer.mp4"
        )

        // Act
        val uiState = movie.toUiState()

        // Assert
        assertThat(uiState.id).isEqualTo(movie.id)
        assertThat(uiState.title).isEqualTo(movie.title)
        assertThat(uiState.rating).isEqualTo(movie.rating)
        assertThat(uiState.posterPath).isEqualTo(movie.posterPath)
        assertThat(uiState.genres).containsExactly("Sci-Fi", "Action")
        assertThat(uiState.overview).isEqualTo(movie.overview)
        assertThat(uiState.releaseDate)
            .isEqualTo(TimeUtil.convertLongToNamedDate(movie.releaseDate))
        assertThat(uiState.runtimeMinutes)
            .isEqualTo(TimeUtil.convertIntToHourMinuteFormat(movie.runtimeMinutes))
        assertThat(uiState.trailerPath).isEqualTo(movie.trailerPath)
    }
}