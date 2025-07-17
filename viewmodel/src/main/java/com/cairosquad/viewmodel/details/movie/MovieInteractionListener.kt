package com.cairosquad.viewmodel.details.movie

interface MovieInteractionListener {
    fun onBackClick()
    fun onShareClick()
    fun onFavoriteClick()
    fun onRateItClick()
    fun onPlayClick()
    fun onAddToListClick()
    fun onReadMoreClick()
    fun onSeeAllCastClick()
    fun onActorClick(actorId: Long)
    fun onSeeAllReviewsClick()
    fun onSeeAllSimilarMoviesClick()
    fun onMovieClick(movieId: Long)
}