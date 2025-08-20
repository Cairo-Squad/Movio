package com.cairosquad.viewmodel.library

import com.cairosquad.viewmodel.exception.ErrorStatus

data class LibraryScreenState(
    val screenStatus: SectionStatus = SectionStatus.LOADING,
    val listsSectionState: SectionStatus = SectionStatus.LOADING,
    val favoritesSectionState: SectionStatus = SectionStatus.LOADING,
    val historySectionState: SectionStatus = SectionStatus.LOADING,
    val dataRequestStatus: SectionStatus = SectionStatus.LOADING,

    val isUserAuthed: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorStatus: ErrorStatus? = null,

    val movieLists: List<ListsUiState> = emptyList(),
    val seriesLists: List<ListsUiState> = emptyList(),
    val favoriteSeries: List<SeriesUiState> = emptyList(),
    val favoriteMovies: List<MovieUiState> = emptyList(),
    val historySeries: List<SeriesUiState> = emptyList(),
    val historyMovies: List<MovieUiState> = emptyList(),
) {

    data class ListsUiState(
        val id: Long,
        val name: String,
        val mediaCount: Long
    )

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
        val releaseDate: String = "0",
        val runtimeMinutes: String? = null,
        val trailerPath: String = ""
    )

    enum class SectionStatus {
        LOADING,
        SUCCESS,
        ERROR
    }
}