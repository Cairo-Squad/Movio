package com.cairosquad.viewmodel.library.view_all_history
import com.cairosquad.viewmodel.exception.ErrorStatus

import com.google.common.truth.Truth.assertThat
import org.junit.Test
class ViewAllHistoryScreenStateTest {
    @Test
    fun `default state should have expected values`() {
        val state = ViewAllHistoryScreenState()

        assertThat(state.screenStatus)
            .isEqualTo(ViewAllHistoryScreenState.SectionStatus.LOADING)
        assertThat(state.errorStatus).isNull()
        assertThat(state.isRefreshing).isFalse()
        assertThat(state.movies).isEmpty()
        assertThat(state.deletedMoviesIds).isEmpty()
        assertThat(state.series).isEmpty()
        assertThat(state.deletedSeriesIds).isEmpty()
    }

    @Test
    fun `custom state should store values correctly`() {
        val movieUiState = ViewAllHistoryScreenState.MovieUiState(
            id = 1,
            title = "Inception",
            rating = 8.8f,
            posterPath = "/poster.jpg",
            genres = listOf("Sci-Fi", "Action"),
            overview = "Dream heist",
            releaseDate = "2010",
            runtimeMinutes = "2h 28m",
            trailerPath = "/trailer.mp4"
        )

        val seriesUiState = ViewAllHistoryScreenState.SeriesUiState(
            id = 2,
            title = "Breaking Bad",
            rating = 9.5f,
            posterPath = "/bb.jpg",
            genres = listOf("Drama", "Crime"),
            seasonsCount = 5,
            releaseDate = "2008",
            overview = "A teacher becomes a drug kingpin",
            trailerPath = "/bb_trailer.mp4"
        )

        val state = ViewAllHistoryScreenState(
            screenStatus = ViewAllHistoryScreenState.SectionStatus.SUCCESS,
            errorStatus = ErrorStatus.NETWORK_ERROR,
            isRefreshing = true,
            movies = listOf(movieUiState),
            deletedMoviesIds = listOf(1L, 3L),
            series = listOf(seriesUiState),
            deletedSeriesIds = listOf(2L, 4L)
        )

        assertThat(state.screenStatus).isEqualTo(ViewAllHistoryScreenState.SectionStatus.SUCCESS)
        assertThat(state.errorStatus).isEqualTo(ErrorStatus.NETWORK_ERROR)
        assertThat(state.isRefreshing).isTrue()
        assertThat(state.movies).containsExactly(movieUiState)
        assertThat(state.deletedMoviesIds).containsExactly(1L, 3L)
        assertThat(state.series).containsExactly(seriesUiState)
        assertThat(state.deletedSeriesIds).containsExactly(2L, 4L)
    }

    @Test
    fun `copy should replace only specified values`() {
        val original = ViewAllHistoryScreenState(
            screenStatus = ViewAllHistoryScreenState.SectionStatus.SUCCESS,
            isRefreshing = false
        )

        val modified = original.copy(isRefreshing = true)

        assertThat(modified.isRefreshing).isTrue()
        assertThat(modified.screenStatus)
            .isEqualTo(original.screenStatus) // unchanged
    }
}