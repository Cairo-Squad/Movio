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
    uiState: SeriesDetailsScreenState,
    viewModel: SeriesDetailsViewModel,
    seriesId: Long,
) {
    val context = LocalContext.current

    val seriesUrl = "$SERIES_URL${seriesId}"
    val message = stringResource(R.string.check_out_this_amazing_series)
    val encodedMessageAndUrl = Uri.encode("$message $seriesUrl")
    AnimatedVisibility(
        visible = uiState.showShareBottomSheet,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        ShareBottomSheet(
            isVisible = uiState.showShareBottomSheet,
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
        visible = uiState.showLoginBottomSheet,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LoginBottomSheet(
            isVisible = uiState.showLoginBottomSheet,
            onDismiss = viewModel::onDismissLoginBottomSheet,
            onLoginClick = viewModel::onNavigateToLogin
        )
    }
    AnimatedVisibility(
        visible = uiState.showAddToListBottomSheet,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        ListBottomSheet(
            isVisible = uiState.showAddToListBottomSheet,
            onDismiss = viewModel::onDismissAddToListBottomSheet,
            lists = emptyList(),
            onListClicked = {},
            onCreateNewList = viewModel::onCreateListClicked
        )
    }
    AnimatedVisibility(
        visible = uiState.showCreateListBottomSheet,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        CreateListBottomSheet(
            isVisible = uiState.showCreateListBottomSheet,
            onDismiss = viewModel::onDismissCreateListBottomSheet,
            isMovie = false,
            value = uiState.newListName,
            onValueChange = viewModel::onValueChange,
            onSubmit = { viewModel.onDismissCreateListBottomSheet() }
        )
    }
    AnimatedVisibility(
        visible = uiState.showRateBottomSheet,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        RateBottomSheet(
            isVisible = uiState.showRateBottomSheet,
            onDismiss = viewModel::onDismissRateBottomSheet,
            rating = uiState.rating,
            imageUrl = BuildConfig.IMAGE_BASE_URL + uiState.series.posterPath,
            name = uiState.series.title,
            isMovie = false,
            onRatingChange = viewModel::onRateChange,
            onSubmitClicked = viewModel::onSubmitRateClicked,
        )
    }
}