package com.cairosquad.viewmodel.details.similar_series

interface SimilarSeriesInteractionListener {
    fun onBackClick()
    fun onSeriesClick(seriesId: Long)
    fun onRefresh(seriesId: Long)
}