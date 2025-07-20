package com.cairosquad.viewmodel.home

import com.cairosquad.entity.Genre
import com.cairosquad.viewmodel.details.artist.ArtistScreenState
import com.cairosquad.viewmodel.exception.ErrorStatus

class HomeScreenState (
    val Movies :List<ArtistScreenState.MovieUiState> = emptyList(),
    val Series :List<ArtistScreenState.SeriesUiState> =emptyList(),
    val screenStatus: ArtistScreenState.ScreenStatus = ArtistScreenState.ScreenStatus.LOADING,
    val errorStatus: ErrorStatus? = null,
) {
    data class MovieUiState(
        val id: Long = 0L,
        val title: String = "",
        val rating: Float = 0f,
        val posterPath: String = "",
        val genres: List<Genre> =emptyList()
    )

    data class SeriesUiState(
        val id: Long = 0L,
        val title: String = "",
        val rating: Float = 0f,
        val posterPath: String = "",
        val genres: List<Genre> = emptyList()
    )

    enum class ScreenStatus {
        LOADING,
        SUCCESS,
        FAILED
    }
}