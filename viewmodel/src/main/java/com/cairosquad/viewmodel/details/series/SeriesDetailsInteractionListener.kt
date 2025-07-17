package com.cairosquad.viewmodel.details.series

interface SeriesDetailsInteractionListener {

    fun onBackClicked()
    fun onShareClicked()
    fun onFavoriteClicked()
    fun onRateClicked()
    fun onPlayTrailerClicked()
    fun onAddToListClicked()
    fun onArtistClicked(artistId: Long)
    fun onSeeAllArtistsClicked(seriesId: Long)
    fun onSeasonClicked(seriesId: Long, seasonNumber: Int)
    fun onSeeAllSeasonsClicked(seriesId: Long)
    fun onSeeAllReviewsClicked(seriesId: Long)
    fun onSeriesClicked(seriesId: Long)
    fun onSeeAllSimilarClicked(seriesId: Long)
}