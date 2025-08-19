package com.cairosquad.viewmodel.details.series

interface SeriesDetailsInteractionListener {

    fun onBackClick()
    fun onShareClick()
    fun onFavoriteClick()
    fun onRateClick()
    fun onPlayTrailerClick()
    fun onAddToListClick()
    fun onCreateListClick()

    fun onDismissShareBottomSheet()
    fun onDismissLoginBottomSheet()
    fun onDismissRateBottomSheet()
    fun onDismissAddToListBottomSheet()
    fun onDismissCreateListBottomSheet()
    fun onDismissSuccessRatedBottomSheet()

    fun onValueChange(listName: String)

    fun onRateChange(rate: Int)
    fun onSubmitRateClick(rate: Int)
    fun onCopy(messageId: Int, isSuccessful: Boolean)

    fun onArtistClick(artistId: Long)
    fun onSeeAllArtistsClick(seriesId: Long)

    fun onSeasonClick(seriesId: Long, seasonNumber: Int)
    fun onSeeAllSeasonsClick(seriesId: Long)

    fun onSeeAllReviewsClick(seriesId: Long)

    fun onSeriesClick(seriesId: Long)
    fun onSeeAllSimilarClick(seriesId: Long)
    fun onNavigateToLogin()
    fun onRefresh()
}