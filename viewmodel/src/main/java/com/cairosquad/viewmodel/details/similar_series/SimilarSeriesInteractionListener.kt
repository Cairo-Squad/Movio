package com.cairosquad.viewmodel.details.similar_series

interface SimilarSeriesInteractionListener {
    fun onClickBack()
    fun onSeriesClicked(seriesId: Long)
    fun onRefresh(seriesId: Long)
}