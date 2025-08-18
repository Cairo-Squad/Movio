package com.cairosquad.viewmodel.details.similar_movies

interface SimilarMoviesInteractionListener {
    fun onBackClick()
    fun onMovieClick(movieId: Long)
    fun onRefresh(movieId: Long)
}