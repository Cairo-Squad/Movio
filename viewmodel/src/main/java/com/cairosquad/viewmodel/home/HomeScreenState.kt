package com.cairosquad.viewmodel.home

import com.cairosquad.entity.Genre
import com.cairosquad.viewmodel.exception.ErrorStatus

class HomeScreenState (
    val Movies :List<HomeScreenState.MovieUiState> = emptyList(),
    val Series :List<HomeScreenState.SeriesUiState> =emptyList(),
    val screenStatus: HomeScreenState.ScreenStatus = HomeScreenState.ScreenStatus.LOADING,
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