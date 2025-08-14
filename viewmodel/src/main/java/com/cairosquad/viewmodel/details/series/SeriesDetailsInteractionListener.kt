package com.cairosquad.viewmodel.details.series

interface SeriesDetailsInteractionListener {

    fun onBackClicked()
    fun onShareClicked()
    fun onFavoriteClicked()
    fun onRateClicked()
    fun onPlayTrailerClicked()
    fun onAddToListClicked()
    fun onCreateListClicked()

    fun onDismissShareBottomSheet()
    fun onDismissLoginBottomSheet()
    fun onDismissRateBottomSheet()
    fun onDismissAddToListBottomSheet()
    fun onDismissCreateListBottomSheet()
    fun onDismissSuccessRatedBottomSheet()

    fun onValueChange(listName: String)

    fun onRateChange(rate: Int)
    fun onSubmitRateClicked(rate: Int)
    fun onCopy(messageId: Int, isSuccessful: Boolean)

    fun onArtistClicked(artistId: Long)
    fun onSeeAllArtistsClicked(seriesId: Long)

    fun onSeasonClicked(seriesId: Long, seasonNumber: Int)
    fun onSeeAllSeasonsClicked(seriesId: Long)

    fun onSeeAllReviewsClicked(seriesId: Long)

    fun onSeriesClicked(seriesId: Long)
    fun onSeeAllSimilarClicked(seriesId: Long)
    fun onNavigateToLogin()
    fun onRefresh()
}