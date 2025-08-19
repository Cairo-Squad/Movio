package com.cairosquad.viewmodel.details.movie

interface MovieInteractionListener {

    fun onBackClick()
    fun onShareClick()
    fun onFavoriteClick()

    fun onRateItClick()
    fun onPlayClick()
    fun onAddToListClick()
    fun onCreateListClick()
    fun onClickList(id: Long)
    fun onSubmitCreateListClick()
    fun onDismissCreateListBottomSheet()
    fun onListValueChange(listName: String)

    fun onSeeAllCastClick(movieId: Long)
    fun onActorClick(actorId: Long)

    fun onSeeAllReviewsClick(movieId: Long)

    fun onSeeAllSimilarMoviesClick(movieId: Long)
    fun onMovieClick(movieId: Long)

    fun onCopy()

    fun onDismissShareBottomSheet()
    fun onDismissLoginBottomSheet()
    fun onDismissRateBottomSheet()
    fun onDismissAddToListBottomSheet()
    fun onDismissRateSuccessBottomSheet()

    fun onRateChange(rate: Int)
    fun onSubmitRateClick(rate: Int)
    fun onNavigateToLogin()

    fun onRefresh()
}