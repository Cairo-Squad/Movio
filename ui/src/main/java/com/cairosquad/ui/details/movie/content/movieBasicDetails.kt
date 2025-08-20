package com.cairosquad.ui.details.movie.content

import androidx.compose.foundation.lazy.LazyListScope
import com.cairosquad.ui.details.composable.BasicDetails
import com.cairosquad.ui.details.composable.BasicDetailsLoading
import com.cairosquad.viewmodel.details.movie.MovieInteractionListener
import com.cairosquad.viewmodel.details.movie.MovieScreenState

fun LazyListScope.movieBasicDetails(
    state: MovieScreenState,
    listener: MovieInteractionListener
) {
    item {
        when (state.basicDetailsSectionState) {
            MovieScreenState.ScreenStatus.LOADING -> {
                BasicDetailsLoading()
            }

            MovieScreenState.ScreenStatus.SUCCESS -> {
                BasicDetails(
                    title = state.movie.title,
                    genres = state.movie.genres,
                    rating = state.movie.rating,
                    releaseDate = state.movie.releaseDate,
                    runtimeMinutes = state.movie.runtimeMinutes,
                    onRateClicked = listener::onRateItClick,
                    onPlayTrailerClicked = listener::onPlayClick,
                    onAddToListClicked = listener::onAddToListClick,
                    isRated = state.isRated
                )
            }

            MovieScreenState.ScreenStatus.ERROR -> {}
        }
    }
}