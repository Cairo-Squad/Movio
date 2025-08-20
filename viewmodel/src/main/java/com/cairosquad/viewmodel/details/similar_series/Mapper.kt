package com.cairosquad.viewmodel.details.similar_series

import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.details.similar_series.SimilarSeriesScreenState.SimilarSeriesUiState
import com.cairosquad.viewmodel.util.localizeNumbers
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace

fun Series.toUiState(): SimilarSeriesUiState {
    return SimilarSeriesUiState(
        id = id,
        title = title.localizeNumbers(),
        posterUrl = posterPath,
        rating = rating.roundToFirstDecimalPlace()

    )
}