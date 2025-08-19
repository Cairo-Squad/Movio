package com.cairosquad.ui.details.series.content

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.cairosquad.design_system.R
import com.cairosquad.ui.BuildConfig
import com.cairosquad.ui.details.Constants.SERIES_URL
import com.cairosquad.ui.movio_component.bottom_sheet.CreateListBottomSheet
import com.cairosquad.ui.movio_component.bottom_sheet.FinishRatingBottomSheet
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

    ShareBottomSheet(
        isVisible = state.showShareBottomSheet,
        onDismiss = viewModel::onDismissShareBottomSheet,
        onCopyLinkClick = {
            ShareUtil.copyLink(
                seriesUrl = seriesUrl,
                context = context,
                isSeries = true,
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
    LoginBottomSheet(
        isVisible = state.showLoginBottomSheet,
        onDismiss = viewModel::onDismissLoginBottomSheet,
        onLoginClick = viewModel::onNavigateToLogin
    )
    ListBottomSheet(
        isVisible = state.showAddToListBottomSheet,
        onDismiss = viewModel::onDismissAddToListBottomSheet,
        lists = emptyList(),
        onListClicked = {},
        onCreateNewList = viewModel::onCreateListClick
    )
    FinishRatingBottomSheet(
        isVisible = state.showSuccessRatedBottomSheet,
        onDismiss = viewModel::onDismissSuccessRatedBottomSheet,
        rating = state.userRating
    )
    CreateListBottomSheet(
        isVisible = state.showCreateListBottomSheet,
        onDismiss = viewModel::onDismissCreateListBottomSheet,
        isMovie = false,
        value = state.newListName,
        onValueChange = viewModel::onValueChange,
        onSubmit = { viewModel.onDismissCreateListBottomSheet() },
    )
    RateBottomSheet(
        isVisible = state.showRateBottomSheet,
        onDismiss = viewModel::onDismissRateBottomSheet,
        rating = state.rating,
        imageUrl = BuildConfig.IMAGE_BASE_URL + state.series.posterPath,
        name = state.series.title,
        isMovie = false,
        onRatingChange = viewModel::onRateChange,
        onSubmitClicked = viewModel::onSubmitRateClick,
    )

}