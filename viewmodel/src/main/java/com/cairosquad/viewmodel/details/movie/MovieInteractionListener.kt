package com.cairosquad.viewmodel.details.movie

interface MovieInteractionListener {

    fun onBackClick()
    fun onShareClick()
    fun onFavoriteClick()

    fun onRateItClick()
    fun onPlayClick()
    fun onAddToListClick()
    fun onCreateListClicked()
    fun onClickList(id: Long)
    fun onSubmitCreateListClicked()
    fun onDismissCreateListBottomSheet()
    fun onListValueChange(listName: String)

    fun onSeeAllCastClick(movieId: Long)
    fun onActorClick(actorId: Long)

    fun onSeeAllReviewsClick(movieId: Long)

    fun onSeeAllSimilarMoviesClick(movieId: Long)
    fun onMovieClick(movieId: Long)

    fun onCopy(messageId: Int, isSuccessful: Boolean)

    fun onDismissShareBottomSheet()
    fun onDismissLoginBottomSheet()
    fun onDismissRateBottomSheet()
    fun onDismissAddToListBottomSheet()

    fun onRateChange(rate: Int)
    fun onSubmitRateClicked(rate: Int)
    fun onNavigateToLogin()

    fun onRefresh()
}