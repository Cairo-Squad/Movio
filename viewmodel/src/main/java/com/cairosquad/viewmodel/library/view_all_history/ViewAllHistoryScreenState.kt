package com.cairosquad.viewmodel.library.view_all_history

import com.cairosquad.viewmodel.exception.ErrorStatus

data class ViewAllHistoryScreenState (
    val screenStatus: SectionStatus = SectionStatus.LOADING,
    val errorStatus: ErrorStatus? = null,

    val isRefreshing: Boolean = false,

    val movies: List<MovieUiState> = emptyList(),
    val deletedMoviesIds: List<Long> = emptyList(),
    val series: List<SeriesUiState> = emptyList(),
    val deletedSeriesIds: List<Long> = emptyList(),
    ) {

    data class SeriesUiState(
        val id: Long = 0L,
        val title: String = "",
        val rating: Float = 0F,
        val posterPath: String = "",
        val genres: List<String> = emptyList(),
        val seasonsCount: Int = 0,
        val releaseDate: String = "",
        val overview: String = "",
        val trailerPath: String = ""
    )

    data class MovieUiState(
        val id: Long = 0,
        val title: String = "",
        val rating: Float = 0f,
        val posterPath: String = "",
        val genres: List<String> = emptyList(),
        val overview: String = "",
        val releaseDate: String = "",
        val runtimeMinutes: String = "",
        val trailerPath: String = ""
    )

    enum class SectionStatus {
        LOADING,
        SUCCESS,
        ERROR
    }
}