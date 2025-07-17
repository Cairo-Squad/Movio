package com.cairosquad.viewmodel.details.movie

import com.cairosquad.viewmodel.base.BaseViewModel

class MovieViewModel(

) : BaseViewModel<MovieScreenState, MovieEffect>(MovieScreenState()), MovieInteractionListener {

    init {
        loadMovieData()
    }

    private fun loadMovieData() {
        // TODO()

    }

    override fun onBackClick() {
        TODO("Not yet implemented")
    }

    override fun onShareClick() {
        TODO("Not yet implemented")
    }

    override fun onFavoriteClick() {
        TODO("Not yet implemented")
    }

    override fun onRateItClick() {
        TODO("Not yet implemented")
    }

    override fun onPlayClick() {
        TODO("Not yet implemented")
    }

    override fun onAddToListClick() {
        TODO("Not yet implemented")
    }

    override fun onReadMoreClick() {
        TODO("Not yet implemented")
    }

    override fun onSeeAllCastClick() {
        TODO("Not yet implemented")
    }

    override fun onActorClick(actorId: Long) {
        TODO("Not yet implemented")
    }

    override fun onSeeAllReviewsClick() {
        TODO("Not yet implemented")
    }

    override fun onSeeAllSimilarMoviesClick() {
        TODO("Not yet implemented")
    }

    override fun onMovieClick(movieId: Long) {
        TODO("Not yet implemented")
    }

}