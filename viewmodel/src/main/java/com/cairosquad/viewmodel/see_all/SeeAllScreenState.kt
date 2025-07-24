package com.cairosquad.viewmodel.see_all

import com.cairosquad.viewmodel.exception.ErrorStatus

data class SeeAllScreenState(
    val mediaList: List<MediaUiState> = emptyList(),

    val screenStatus: ScreenStatus = ScreenStatus.LOADING,
    val errorStatus: ErrorStatus? = null,

    val genres: List<GenreUiState> = listOf(GenreUiState.defaultGenre),
    val selectedGenreIndex: Int = 0,
) {
    data class MediaUiState(
        val id: Long,
        val title: String,
        val rating: Float,
        val posterPath: String,
        val genres: List<GenreUiState>,
        val isMovie: Boolean
    )

    data class GenreUiState(
        val id: Long? = 0L,
        val name: String = "",
    ) {
        companion object {
            val defaultGenre = GenreUiState(
                id = null,
                name = "All"
            )
        }
    }

    enum class ScreenStatus {
        LOADING,
        SUCCESS,
        FAILED
    }
}