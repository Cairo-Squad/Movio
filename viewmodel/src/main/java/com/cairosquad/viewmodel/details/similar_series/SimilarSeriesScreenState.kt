package com.cairosquad.viewmodel.details.similar_series

import com.cairosquad.viewmodel.exception.ErrorStatus

data class SimilarSeriesScreenState(
    val series: List<SimilarSeriesUiState> = emptyList(),
    val screenStatus: ScreenStatus = ScreenStatus.INITIAL,
    val errorStatus: ErrorStatus? = null
) {
    data class SimilarSeriesUiState(
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
