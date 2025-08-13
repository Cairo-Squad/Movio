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
    state: MovieScreenState,
    listener: MovieInteractionListener
) {
    item {
        when (state.castSectionState) {
            MovieScreenState.ScreenStatus.LOADING -> {
                SectionLoading(
                    headerName = stringResource(R.string.top_cast),
                    sectionLoadingItem = {
                        LoadingArtistCard()
                    }
                )
            }

            MovieScreenState.ScreenStatus.SUCCESS -> {
                if (state.topCast.isNotEmpty()) {
                    MovieTopCastSection(
                        onActionClicked = {
                            listener.onSeeAllCastClick(
                                state.movie.id
                            )
                        },
                        onArtistClicked = listener::onActorClick,
                        cast = state.topCast,
                    )
                }
            }

            MovieScreenState.ScreenStatus.ERROR -> {}
        }
    }
}