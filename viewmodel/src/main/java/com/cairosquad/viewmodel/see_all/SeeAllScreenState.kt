package com.cairosquad.viewmodel.see_all

import com.cairosquad.viewmodel.R
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.home.HomeScreenState.GenreUiState

data class SeeAllScreenState(
    val mediaList: List<MediaUiState> = emptyList(),

    val screenStatus: ScreenStatus = ScreenStatus.LOADING,
    val errorStatus: ErrorStatus? = null,
    val isLoading: Boolean = false,
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
        val nameResId: Int? = null
    ) {
        companion object {
            val defaultGenre = GenreUiState(
                id = null,
                name ="",
                nameResId = R.string.sorting_type_all
            )
        }
    }

    enum class ScreenStatus {
        LOADING,
        SUCCESS,
        FAILED
    }
}