package com.cairosquad.ui.details.movie.content

import androidx.compose.foundation.lazy.LazyListScope
import com.cairosquad.ui.details.composable.BasicDetails
import com.cairosquad.ui.details.composable.BasicDetailsLoading
import com.cairosquad.viewmodel.details.movie.MovieInteractionListener
import com.cairosquad.viewmodel.details.movie.MovieScreenState

fun LazyListScope.MovieBasicDetails(
    uiState: MovieScreenState,
    interactionListener: MovieInteractionListener
) {
    item {
        when (uiState.basicDetailsSectionState) {
            MovieScreenState.ScreenStatus.LOADING -> {
                BasicDetailsLoading()
            }

            MovieScreenState.ScreenStatus.SUCCESS -> {
                BasicDetails(
                    title = uiState.movie.title,
                    genres = uiState.movie.genres,
                    rating = uiState.movie.rating,
                    releaseDate = uiState.movie.releaseDate,
                    runtimeMinutes = uiState.movie.runtimeMinutes,
                    onRateClicked = interactionListener::onRateItClick,
                    onPlayTrailerClicked = interactionListener::onPlayClick,
                    onAddToListClicked = interactionListener::onAddToListClick,
                    isRated = uiState.isRated
                )
            }

            MovieScreenState.ScreenStatus.ERROR -> {}
        }
    }
}