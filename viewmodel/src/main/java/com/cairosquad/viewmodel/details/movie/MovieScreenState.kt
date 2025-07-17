package com.cairosquad.viewmodel.details.movie

data class MovieScreenState(
    val isLoading: Boolean = true,
    val movie: MovieDetailsUiState = MovieDetailsUiState(),
    val topCast: List<TopCastUiState> = emptyList(),
    val reviews: List<ReviewUiState> = emptyList(),
    val similarMovies: List<SimilarMovieUiState> = emptyList(),
    val isFavorite: Boolean = false,
    val isShareBottomSheetOpen: Boolean = false,
    val isNoAccountBottomSheetOpen: Boolean = false,
    val isRateBottomSheetOpen: Boolean = false,
    val isAddToListBottomSheetOpen: Boolean = false,
    val successStatus: SuccessStatus = SuccessStatus.NONE,
    val errorStatus: ErrorStatus = ErrorStatus.NONE
) {
    data class MovieDetailsUiState(
        val id: Long = 0,
        val title: String = "",
        val rating: Float = 0.0f,
        val posterPath: String = "",
        val genres: List<GenreUiState> = emptyList(),
        val overview: String = "",
        val releaseDate: String = "0",
        val runtimeMinutes: Int = 0,
    )

    data class GenreUiState(
        val id: Long = 0,
        val name: String = ""
    )

    data class TopCastUiState(
        val id: Long = 0,
        val name: String = "",
        val photoPath: String = ""
    )

    data class ReviewUiState(
        val id: Long = 0,
        val author: String = "",
        val authorPhotoPath: String = "",
        val rating: Float = 0.0f,
        val date: String,
        val description: String,
    )

    data class SimilarMovieUiState(
        val id: Long = 0,
        val title: String = "",
        val rating: Float = 0.0f,
        val posterPath: String = "",
    )

    enum class SuccessStatus {
        NONE,
        SHARE,
        RATE,
        ADD_TO_LIST,
        // TODO: ADD?
    }

    enum class ErrorStatus {
        NONE,
        NO_INTERNET,
        UNAUTHORIZED,
        UNKNOWN,
        // TODO: ADD?
    }
}
