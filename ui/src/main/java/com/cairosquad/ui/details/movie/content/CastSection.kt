package com.cairosquad.ui.details.movie.content

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.res.stringResource
import com.cairosquad.design_system.R
import com.cairosquad.ui.details.composable.MovieTopCastSection
import com.cairosquad.ui.details.composable.SectionLoading
import com.cairosquad.ui.movio_component.LoadingArtistCard
import com.cairosquad.viewmodel.details.movie.MovieInteractionListener
import com.cairosquad.viewmodel.details.movie.MovieScreenState

fun LazyListScope.CastSection(
    uiState: MovieScreenState,
    interactionListener: MovieInteractionListener
) {
    item {
        when (uiState.castSectionState) {
            MovieScreenState.ScreenStatus.LOADING -> {
                SectionLoading(
                    headerName = stringResource(R.string.top_cast),
                    sectionLoadingItem = {
                        LoadingArtistCard()
                    }
                )
            }

            MovieScreenState.ScreenStatus.SUCCESS -> {
                if (uiState.topCast.isNotEmpty()) {
                    MovieTopCastSection(
                        onActionClicked = {
                            interactionListener.onSeeAllCastClick(
                                uiState.movie.id
                            )
                        },
                        onArtistClicked = interactionListener::onActorClick,
                        cast = uiState.topCast,
                    )
                }
            }

            MovieScreenState.ScreenStatus.ERROR -> {}
        }
    }
}