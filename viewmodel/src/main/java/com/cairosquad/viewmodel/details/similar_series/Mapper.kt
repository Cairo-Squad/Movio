package com.cairosquad.viewmodel.details.similar_series

import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.details.similar_series.SimilarSeriesScreenState.SimilarSeriesUiState

fun Series.toUiState(): SimilarSeriesUiState {
    return SimilarSeriesUiState(
        id = id,
        title = title,
        posterUrl = posterPath,
        rating = rating

    )
}