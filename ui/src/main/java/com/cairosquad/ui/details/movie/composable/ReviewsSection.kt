package com.cairosquad.ui.details.movie.composable

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.ui.details.composable.MovieReviewSection
import com.cairosquad.ui.details.composable.SectionLoading
import com.cairosquad.ui.movio_component.LoadingReviewCard
import com.cairosquad.viewmodel.details.movie.MovieInteractionListener
import com.cairosquad.viewmodel.details.movie.MovieScreenState

fun LazyListScope.ReviewsSection(
    state: MovieScreenState,
    listener: MovieInteractionListener
) {
    item {
        when (state.reviewsSectionState) {
            MovieScreenState.ScreenStatus.LOADING -> {
                SectionLoading(
                    headerName = stringResource(R.string.reviews),
                    sectionLoadingItem = {
                        LoadingReviewCard(
                            modifier = Modifier.size(
                                width = 260.dp,
                                height = 137.dp
                            )
                        )
                    }
                )
            }

            MovieScreenState.ScreenStatus.SUCCESS -> {
                if (state.reviews.isNotEmpty()) {
                    MovieReviewSection(
                        reviews = state.reviews,
                        onActionClicked = {
                            listener.onSeeAllReviewsClick(
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