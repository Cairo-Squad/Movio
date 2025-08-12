package com.cairosquad.ui.details.movie.content

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.cairosquad.design_system.R
import com.cairosquad.ui.BuildConfig
import com.cairosquad.ui.details.Constants.MOVIE_URL
import com.cairosquad.ui.movio_component.bottom_sheet.CreateListBottomSheet
import com.cairosquad.ui.movio_component.bottom_sheet.ListBottomSheet
import com.cairosquad.ui.movio_component.bottom_sheet.LoginBottomSheet
import com.cairosquad.ui.movio_component.bottom_sheet.RateBottomSheet
import com.cairosquad.ui.movio_component.bottom_sheet.ShareBottomSheet
import com.cairosquad.ui.utils.ShareUtil
import com.cairosquad.viewmodel.details.movie.MovieScreenState
import com.cairosquad.viewmodel.details.movie.MovieViewModel

@Composable
fun MovieScreenBottomSheets(
    state: MovieScreenState,
    viewModel: MovieViewModel,
    movieId: Long,
) {
    val context = LocalContext.current
    val movieUrl = "$MOVIE_URL${movieId}"
    val message = stringResource(R.string.check_out_this_amazing_movie)
    val encodedMessageAndUrl = Uri.encode("$message $movieUrl")

    ShareBottomSheet(
        isVisible = state.isShareBottomSheetOpen,
        onDismiss = viewModel::onDismissShareBottomSheet,
        onCopyLinkClick = {
            ShareUtil.copyLink(
                seriesUrl = movieUrl,
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
    LoginBottomSheet(
        isVisible = state.isNoAccountBottomSheetOpen,
        onDismiss = viewModel::onDismissLoginBottomSheet,
        onLoginClick = viewModel::onNavigateToLogin
    )
    ListBottomSheet(
        isVisible = state.isAddToListBottomSheetOpen,
        onDismiss = viewModel::onDismissAddToListBottomSheet,
        lists = state.moviesLists.map { it.name },
        onListClicked = { index ->
            viewModel.onClickList(state.moviesLists[index].id)
        },
        onCreateNewList = viewModel::onCreateListClicked
    )
    CreateListBottomSheet(
        isVisible = state.showCreateListBottomSheet,
        onDismiss = viewModel::onDismissCreateListBottomSheet,
        value = state.listName,
        onValueChange = viewModel::onListValueChange,
        onSubmit = { viewModel.onSubmitCreateListClicked() },
        isMovie = true
    )
    RateBottomSheet(
        isVisible = state.isRateBottomSheetOpen,
        onDismiss = viewModel::onDismissRateBottomSheet,
        rating = state.rate,
        imageUrl = BuildConfig.IMAGE_BASE_URL + state.movie.posterPath,
        name = state.movie.title,
        isMovie = true,
        onRatingChange = viewModel::onRateChange,
        onSubmitClicked = viewModel::onSubmitRateClicked,
    )
}