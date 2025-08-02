package com.cairosquad.viewmodel.details.similar_movies

interface SimilarMoviesInteractionListener {
    fun onClickBack()
    fun onMovieClicked(movieId: Long)

    fun onRefresh(movieId: Long)
}