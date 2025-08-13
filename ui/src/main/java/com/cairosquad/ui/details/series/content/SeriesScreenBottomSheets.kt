package com.cairosquad.ui.details.series.content

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.cairosquad.design_system.R
import com.cairosquad.ui.BuildConfig
import com.cairosquad.ui.details.Constants.SERIES_URL
import com.cairosquad.ui.movio_component.bottom_sheet.CreateListBottomSheet
import com.cairosquad.ui.movio_component.bottom_sheet.ListBottomSheet
import com.cairosquad.ui.movio_component.bottom_sheet.LoginBottomSheet
import com.cairosquad.ui.movio_component.bottom_sheet.RateBottomSheet
import com.cairosquad.ui.movio_component.bottom_sheet.ShareBottomSheet
import com.cairosquad.ui.utils.ShareUtil
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState
import com.cairosquad.viewmodel.details.series.SeriesDetailsViewModel

@Composable
fun SeriesScreenBottomSheets(
    state: SeriesDetailsScreenState,
    viewModel: SeriesDetailsViewModel,
) {
    val context = LocalContext.current

    val seriesUrl = "$SERIES_URL${state.series.id}"
    val message = stringResource(R.string.check_out_this_amazing_series)
    val encodedMessageAndUrl = Uri.encode("$message $seriesUrl")
    AnimatedVisibility(
        visible = state.showShareBottomSheet,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        ShareBottomSheet(
            isVisible = state.showShareBottomSheet,
            onDismiss = viewModel::onDismissShareBottomSheet,
            onCopyLinkClick = {
                ShareUtil.copyLink(
                    seriesUrl = seriesUrl,
                    context = context,
                    onDismiss = viewModel::onCopy
                )
            },
            onShareFacebookClick = {
                ShareUtil.shareOnFacebook(
                    encodedMessageAndUrl = encodedMessageAndUrl,
                    context = context,
                    onDismiss = viewModel::onDismissShareBottomSheet
                )
            },
            onShareXClick = {
                ShareUtil.shareOnX(
                    encodedMessageAndUrl = encodedMessageAndUrl,
                    context = context,
                    onDismiss = viewModel::onDismissShareBottomSheet
                )
            }
        )
    }
    AnimatedVisibility(
        visible = state.showLoginBottomSheet,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LoginBottomSheet(
            isVisible = state.showLoginBottomSheet,
            onDismiss = viewModel::onDismissLoginBottomSheet,
            onLoginClick = viewModel::onNavigateToLogin
        )
    }
    AnimatedVisibility(
        visible = state.showAddToListBottomSheet,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        ListBottomSheet(
            isVisible = state.showAddToListBottomSheet,
            onDismiss = viewModel::onDismissAddToListBottomSheet,
            lists = emptyList(),
            onListClicked = {},
            onCreateNewList = viewModel::onCreateListClicked
        )
    }
    AnimatedVisibility(
        visible = state.showCreateListBottomSheet,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        CreateListBottomSheet(
            isVisible = state.showCreateListBottomSheet,
            onDismiss = viewModel::onDismissCreateListBottomSheet,
            isMovie = false,
            value = state.newListName,
            onValueChange = viewModel::onValueChange,
            onSubmit = { viewModel.onDismissCreateListBottomSheet() }
        )
    }
    AnimatedVisibility(
        visible = state.showRateBottomSheet,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        RateBottomSheet(
            isVisible = state.showRateBottomSheet,
            onDismiss = viewModel::onDismissRateBottomSheet,
            rating = state.rating,
            imageUrl = BuildConfig.IMAGE_BASE_URL + state.series.posterPath,
            name = state.series.title,
            isMovie = false,
            onRatingChange = viewModel::onRateChange,
            onSubmitClicked = viewModel::onSubmitRateClicked,
        )
    }
}