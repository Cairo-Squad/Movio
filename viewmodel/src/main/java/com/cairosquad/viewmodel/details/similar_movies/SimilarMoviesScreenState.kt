package com.cairosquad.viewmodel.details.similar_movies

import com.cairosquad.viewmodel.exception.ErrorStatus

data class SimilarMoviesScreenState(
    val movies: List<SimilarMovieUiState> = emptyList(),
    val screenStatus: ScreenStatus = ScreenStatus.INITIAL,
    val errorStatus: ErrorStatus? = null
) {
    data class SimilarMovieUiState(
        val id: Long,
        val title: String,
        val posterUrl: String,
        val rating: Float
    )

    enum class ScreenStatus {
        INITIAL,
        LOADING,
        ERROR,
        SUCCESS
    }
}
