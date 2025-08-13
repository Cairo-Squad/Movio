package com.cairosquad.ui.details.movie.content

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.ui.details.composable.SectionLoading
import com.cairosquad.ui.details.composable.SimilarMoviesSection
import com.cairosquad.ui.movio_component.LoadingMovieCard
import com.cairosquad.viewmodel.details.movie.MovieInteractionListener
import com.cairosquad.viewmodel.details.movie.MovieScreenState

fun LazyListScope.SimilarMoviesSection(
    state: MovieScreenState,
    listener: MovieInteractionListener
) {
    item {
        when (state.similarMoviesSectionState) {
            MovieScreenState.ScreenStatus.LOADING -> {
                SectionLoading(
                    headerName = stringResource(R.string.similar_movies),
                    sectionLoadingItem = {
                        LoadingMovieCard(
                            height = 160.dp
                        )
                    }
                )
            }

            MovieScreenState.ScreenStatus.SUCCESS -> {
                if (state.similarMovies.isNotEmpty()) {
                    SimilarMoviesSection(
                        similarMovies = state.similarMovies,
                        onMovieClicked = listener::onMovieClick,
                        onActionClicked = {
                            listener.onSeeAllSimilarMoviesClick(
                                state.movie.id
                            )
                        }
                    )
                }
            }

            MovieScreenState.ScreenStatus.ERROR -> {}
        }
    }
}