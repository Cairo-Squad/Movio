package com.cairosquad.viewmodel.details.movie

import com.cairosquad.viewmodel.exception.ErrorStatus

data class MovieScreenState(
    val basicDetailsSectionState: ScreenStatus = ScreenStatus.INITIAL,
    val castSectionState: ScreenStatus = ScreenStatus.INITIAL,
    val reviewsSectionState: ScreenStatus = ScreenStatus.INITIAL,
    val similarMoviesSectionState: ScreenStatus = ScreenStatus.INITIAL,

    val isLoading: Boolean = true,
    val movie: MovieDetailsUiState = MovieDetailsUiState(),
    val topCast: List<TopCastUiState> = emptyList(),
    val reviews: List<ReviewUiState> = emptyList(),
    val similarMovies: List<MovieDetailsUiState> = emptyList(),
    val isFavorite: Boolean = false,
    val isShareBottomSheetOpen: Boolean = false,
    val isNoAccountBottomSheetOpen: Boolean = false,
    val isRateBottomSheetOpen: Boolean = false,
    val isAddToListBottomSheetOpen: Boolean = false,
    val errorStatus: ErrorStatus? = null,

    val showSnackBar: Boolean = false,
    val snackMessage: String = "",
    val isProcessSuccess: Boolean = false,
) {
    data class MovieDetailsUiState(
        val id: Long = 0,
        val title: String = "",
        val rating: Float = 0.0f,
        val posterPath: String = "",
        val genres: List<String> = emptyList(),
        val overview: String = "",
        val releaseDate: String = "0",
        val runtimeMinutes: String = "",
        val trailerPath: String = ""
    )

    data class TopCastUiState(
        val id: Long = 0,
        val name: String = "",
        val photoPath: String = ""
    )

    data class ReviewUiState(
        val id: String = "",
        val author: String = "",
        val authorPhotoPath: String = "",
        val rating: Float = 0.0f,
        val date: String,
        val description: String,
    )

    enum class ScreenStatus {
        INITIAL,
        LOADING,
        SUCCESS,
        ERROR
    }
}
