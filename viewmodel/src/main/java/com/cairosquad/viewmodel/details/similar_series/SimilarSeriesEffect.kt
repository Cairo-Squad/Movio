package com.cairosquad.viewmodel.details.similar_series

sealed class SimilarSeriesEffect {

    data class NavigateToSeriesDetails(val seriesId: Long) : SimilarSeriesEffect()
    data object NavigateBack : SimilarSeriesEffect()

}