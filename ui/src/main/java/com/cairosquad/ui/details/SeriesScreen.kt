package com.cairosquad.ui.details

import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.ExpandableText
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.basic_component.SnackBar
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.BuildConfig
import com.cairosquad.ui.details.Constants.SERIES_URL
import com.cairosquad.ui.details.composable.BasicDetails
import com.cairosquad.ui.details.composable.BasicDetailsLoading
import com.cairosquad.ui.details.composable.SeasonSection
import com.cairosquad.ui.details.composable.SectionLoading
import com.cairosquad.ui.details.composable.SeriesReviewSection
import com.cairosquad.ui.details.composable.SeriesTopCastSection
import com.cairosquad.ui.details.composable.SimilarSeriesSection
import com.cairosquad.ui.movio_component.LoadingArtistCard
import com.cairosquad.ui.movio_component.LoadingMovieCard
import com.cairosquad.ui.movio_component.LoadingMovieImage
import com.cairosquad.ui.movio_component.LoadingReviewCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.movio_component.bottom_sheet.CreateListBottomSheet
import com.cairosquad.ui.movio_component.bottom_sheet.ListBottomSheet
import com.cairosquad.ui.movio_component.bottom_sheet.LoginBottomSheet
import com.cairosquad.ui.movio_component.bottom_sheet.RateBottomSheet
import com.cairosquad.ui.movio_component.bottom_sheet.ShareBottomSheet
import com.cairosquad.ui.navigation.ArtistRoute
import com.cairosquad.ui.navigation.EpisodesRoute
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.LoginRoute
import com.cairosquad.ui.navigation.ReviewsRoute
import com.cairosquad.ui.navigation.SeasonsRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.navigation.SimilarSeriesRoute
import com.cairosquad.ui.navigation.TopCastRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.ui.utils.ShareUtil
import com.cairosquad.ui.utils.errorStatusToMessageResource
import com.cairosquad.viewmodel.details.series.SeriesDetailEffect
import com.cairosquad.viewmodel.details.series.SeriesDetailsInteractionListener
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState
import com.cairosquad.viewmodel.details.series.SeriesDetailsViewModel

@Composable
fun SeriesScreen(
    seriesId: Long,
) {
    val viewModel: SeriesDetailsViewModel =
        hiltViewModel<SeriesDetailsViewModel, SeriesDetailsViewModel.Factory> { factory ->
            factory.create(seriesId)
        }
    val navController = LocalNavController.current
    val context = LocalContext.current
    val uiState by viewModel.screenState.collectAsStateWithLifecycle()
    val seriesUrl = "$SERIES_URL${seriesId}"
    val message = stringResource(R.string.check_out_this_amazing_series)
    val encodedMessageAndUrl = Uri.encode("$message $seriesUrl")

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {

            SeriesDetailEffect.NavigateBack -> {
                navController.popBackStack()
            }

            SeriesDetailEffect.PlayTrailer -> {
                if (uiState.series.trailerPath.isBlank()) {
                    viewModel.showSnackBar(
                        message = context.getString(com.cairosquad.ui.R.string.no_trailer_found_for_this_series),
                        isSuccessful = false
                    )
                } else {
                    ShareUtil.playOnYoutube(videoId = uiState.series.trailerPath, context = context)
                }
            }

            is SeriesDetailEffect.ErrorHappened -> {
                viewModel.showSnackBar(
                    message = context.getString(errorStatusToMessageResource(effect.message)),
                    isSuccessful = false
                )
            }

            is SeriesDetailEffect.NavigateToAllArtists -> {
                navController.navigate(TopCastRoute(seriesId, isMovie = false))
            }

            is SeriesDetailEffect.NavigateToAllReviews -> {
                navController.navigate(ReviewsRoute(seriesId, isMovie = false))
            }

            is SeriesDetailEffect.NavigateToAllSeasons -> {
                navController.navigate(SeasonsRoute(seriesId))
            }

            is SeriesDetailEffect.NavigateToAllSimilar -> {
                navController.navigate(SimilarSeriesRoute(seriesId))
            }

            is SeriesDetailEffect.NavigateToArtistDetails -> {
                navController.navigate(ArtistRoute(effect.artistId))
            }

            is SeriesDetailEffect.NavigateToSeasonDetails -> {
                navController.navigate(
                    EpisodesRoute(
                        seriesId = effect.seriesId,
                        seasonNumber = effect.seasonNumber
                    )
                )
            }

            is SeriesDetailEffect.NavigateToSeriesDetails -> {
                navController.navigate(SeriesRoute(effect.seriesId))
            }

            SeriesDetailEffect.NavigateToLogin -> {
                navController.navigate(LoginRoute)
            }
        }
    }
    Box {
        SeriesScreenContent(
            uiState = uiState,
            listener = viewModel
        )
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
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(16.dp),
            visible = uiState.showSnackBar,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> 2 * fullHeight },
                animationSpec = tween(durationMillis = 600)
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> 2 * fullHeight },
                animationSpec = tween(durationMillis = 600)
            )
        ) {
            SnackBar(
                imageVector = ImageVector.vectorResource(if (uiState.isProcessSuccess) R.drawable.archive_tick else R.drawable.danger),
                message = uiState.snackMessage.ifEmpty {
                    stringResource(uiState.snackMessageId)
                },
                action = {}
            )
        }
    }
}

@Composable
private fun SeriesScreenContent(
    uiState: SeriesDetailsScreenState,
    listener: SeriesDetailsInteractionListener
) {
    val listState = rememberScrollState()
    val density = LocalDensity.current
    val scrollThresholdPx = with(density) { 275.dp.toPx() }
    val progress = (listState.value / scrollThresholdPx).coerceIn(0f, 1f)

    val animatedStartColor = lerp(
        start = Theme.color.surfaces.statusBarShadow,
        stop = Theme.color.surfaces.surface,
        fraction = progress
    )
    val animatedEndColor = lerp(
        start = Color.Transparent,
        stop = Theme.color.surfaces.surface,
        fraction = progress
    )
    val animatedBrush = Brush.verticalGradient(
        colors = listOf(animatedStartColor, animatedEndColor)
    )

    when (uiState.basicDetailsSectionState) {
        SeriesDetailsScreenState.SectionStatus.ERROR -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                StateMessage(
                    imageDrawable = R.drawable.no_internet,
                    titleId = R.string.no_internet_connection,
                    descriptionId = R.string.internet_is_not_available_description
                )
            }
        }

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Theme.color.surfaces.surface)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .verticalScroll(listState)
            ) {
                when (uiState.basicDetailsSectionState) {
                    SeriesDetailsScreenState.SectionStatus.LOADING -> {}
                    SeriesDetailsScreenState.SectionStatus.SUCCESS -> {
                        if (uiState.series.posterPath.isNotEmpty()) {
                            Box {
                                SafeImageViewer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(400.dp)
                                        .then(
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                                Modifier.blur(
                                                    16.dp,
                                                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                                                )
                                            } else {
                                                Modifier
                                            }
                                        )
                                        .offset(y = (-28).dp),
                                    model = BuildConfig.IMAGE_BASE_URL + uiState.series.posterPath,
                                    contentDescription = "",
                                    blur = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) 16 else 0,
                                    isBlurForced = true
                                )
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(60.dp)
                                            .align(Alignment.BottomCenter)
                                            .background(
                                                brush = verticalGradient(
                                                    colors = listOf(
                                                        Theme.color.surfaces.surface.copy(alpha = 0.35f),
                                                        Theme.color.surfaces.surface.copy(alpha = 0.50f),
                                                        Theme.color.surfaces.surface.copy(alpha = 0.90f),
                                                        Theme.color.surfaces.surface,
                                                    )
                                                )
                                            )
                                    )
                                }
                            }
                        }
                    }

                    SeriesDetailsScreenState.SectionStatus.ERROR -> {}
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .then(
                            if (
                                uiState.showCreateListBottomSheet
                                || uiState.showRateBottomSheet
                                || uiState.showLoginBottomSheet
                                || uiState.showShareBottomSheet
                                || uiState.showAddToListBottomSheet
                            ) {
                                Modifier.blur(4.dp)
                            } else {
                                Modifier
                            }
                        )
                        .heightIn(max = 10000.dp),
                    horizontalAlignment = Alignment.Start,
                    userScrollEnabled = false
                ) {
                    item {
                        when (uiState.basicDetailsSectionState) {
                            SeriesDetailsScreenState.SectionStatus.LOADING -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 56.dp, bottom = 24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    LoadingMovieImage(
                                        modifier = Modifier.size(height = 260.dp, width = 200.dp)
                                    )
                                }
                            }

                            SeriesDetailsScreenState.SectionStatus.SUCCESS -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 56.dp, bottom = 24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if (uiState.series.posterPath.isNotEmpty())
                                        SafeImageViewer(
                                            modifier = Modifier
                                                .size(height = 260.dp, width = 200.dp)
                                                .clip(RoundedCornerShape(8.dp)),
                                            model = BuildConfig.IMAGE_BASE_URL + uiState.series.posterPath,
                                            contentDescription = "",
                                            loadingPlaceholder = {
                                                LoadingMovieImage(
                                                    modifier = Modifier.size(
                                                        height = 260.dp,
                                                        width = 200.dp
                                                    )
                                                )
                                            }
                                        )
                                    else
                                        Box(
                                            modifier = Modifier
                                                .size(height = 260.dp, width = 200.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Theme.color.system.defaultImageBackground),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                modifier = Modifier.size(24.dp),
                                                imageVector = ImageVector.vectorResource(id = R.drawable.image_icon),
                                                contentDescription = stringResource(R.string.default_image_icon),
                                                tint = Color(0xFFEFF1F5)
                                            )
                                        }
                                }
                            }

                            SeriesDetailsScreenState.SectionStatus.ERROR -> {
                                DetailsFailContent(onTryAgainClick = listener::onRefresh)
                            }
                        }
                    }
                    item {
                        when (uiState.basicDetailsSectionState) {
                            SeriesDetailsScreenState.SectionStatus.LOADING -> {
                                BasicDetailsLoading()
                            }

                            SeriesDetailsScreenState.SectionStatus.SUCCESS -> {
                                BasicDetails(
                                    title = uiState.series.title,
                                    genres = uiState.series.genres,
                                    rating = uiState.series.rating,
                                    releaseDate = uiState.series.releaseDate,
                                    seasonsCount = uiState.series.seasonsCount,
                                    onRateClicked = listener::onRateClicked,
                                    onPlayTrailerClicked = listener::onPlayTrailerClicked,
                                    onAddToListClicked = listener::onAddToListClicked,
                                    isRated = uiState.isRated
                                )
                            }

                            SeriesDetailsScreenState.SectionStatus.ERROR -> {
                                DetailsFailContent(onTryAgainClick = listener::onRefresh)
                            }
                        }
                    }
                    item {
                        when (uiState.basicDetailsSectionState) {
                            SeriesDetailsScreenState.SectionStatus.LOADING -> {
                                LoadingMovieImage(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .fillMaxWidth()
                                        .height(height = 200.dp)
                                        .padding(bottom = 32.dp)
                                )
                            }

                            SeriesDetailsScreenState.SectionStatus.SUCCESS -> {
                                if (uiState.series.overview.isNotEmpty()) {
                                    ExpandableText(
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                            .padding(top = 16.dp),
                                        text = uiState.series.overview,
                                        showMoreText = stringResource(R.string.read_more_with_dots_behind),
                                        showLessText = stringResource(R.string.read_less_with_dots_behind),
                                        color = Theme.color.surfaces.onSurface,
                                        style = Theme.textStyle.label.smallRegular14,
                                        showMoreStyle = Theme.textStyle.label.smallRegular14,
                                        showMoreColor = Theme.color.surfaces.onSurfaceVariant,
                                        showLessColor = Theme.color.surfaces.onSurfaceVariant,
                                    )
                                }
                            }

                            SeriesDetailsScreenState.SectionStatus.ERROR -> {
                                DetailsFailContent(onTryAgainClick = listener::onRefresh)
                            }
                        }
                    }
                    item {
                        when (uiState.castSectionState) {
                            SeriesDetailsScreenState.SectionStatus.LOADING -> {
                                SectionLoading(
                                    headerName = stringResource(R.string.top_cast),
                                    sectionLoadingItem = {
                                        LoadingArtistCard()
                                    }
                                )
                            }

                            SeriesDetailsScreenState.SectionStatus.SUCCESS -> {
                                if (uiState.cast.isNotEmpty()) {
                                    SeriesTopCastSection(
                                        onActionClicked = { listener.onSeeAllArtistsClicked(uiState.series.id) },
                                        onArtistClicked = listener::onArtistClicked,
                                        cast = uiState.cast
                                    )
                                }
                            }

                            SeriesDetailsScreenState.SectionStatus.ERROR -> {
                                DetailsFailContent(onTryAgainClick = listener::onRefresh)
                            }
                        }
                    }
                    item {
                        when (uiState.seasonsSectionState) {
                            SeriesDetailsScreenState.SectionStatus.LOADING -> {
                                SectionLoading(
                                    headerName = stringResource(R.string.current_seasons),
                                    sectionLoadingItem = {
                                        LoadingMovieImage(
                                            modifier = Modifier.size(
                                                width = 260.dp,
                                                height = 137.dp
                                            )
                                        )
                                    }
                                )
                            }

                            SeriesDetailsScreenState.SectionStatus.SUCCESS -> {
                                if (uiState.seasons.isNotEmpty()) {
                                    SeasonSection(
                                        seriesName = uiState.series.title,
                                        seriesId = uiState.series.id,
                                        seasons = uiState.seasons,
                                        onActionClicked = { listener.onSeeAllSeasonsClicked(seriesId = uiState.series.id) },
                                        onSeasonClicked = listener::onSeasonClicked
                                    )
                                }
                            }

                            SeriesDetailsScreenState.SectionStatus.ERROR -> {
                                DetailsFailContent(onTryAgainClick = listener::onRefresh)
                            }
                        }
                    }
                    item {
                        when (uiState.reviewsSectionState) {
                            SeriesDetailsScreenState.SectionStatus.LOADING -> {
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

                            SeriesDetailsScreenState.SectionStatus.SUCCESS -> {
                                if (uiState.reviews.isNotEmpty()) {
                                    SeriesReviewSection(
                                        reviews = uiState.reviews,
                                        onActionClicked = { listener.onSeeAllReviewsClicked(seriesId = uiState.series.id) }
                                    )
                                }
                            }

                            SeriesDetailsScreenState.SectionStatus.ERROR -> {
                                DetailsFailContent(onTryAgainClick = listener::onRefresh)
                            }
                        }
                    }
                    item {
                        when (uiState.similarSeriesSectionState) {
                            SeriesDetailsScreenState.SectionStatus.LOADING -> {
                                SectionLoading(
                                    headerName = stringResource(R.string.similar_series),
                                    sectionLoadingItem = {
                                        LoadingMovieCard(
                                            height = 160.dp
                                        )
                                    }
                                )
                            }

                            SeriesDetailsScreenState.SectionStatus.SUCCESS -> {
                                if (uiState.similarSeries.isNotEmpty()) {
                                    SimilarSeriesSection(
                                        similarSeries = uiState.similarSeries,
                                        onSeriesClicked = listener::onSeriesClicked,
                                        onActionClicked = { listener.onSeeAllSimilarClicked(seriesId = uiState.series.id) },
                                    )
                                }
                            }

                            SeriesDetailsScreenState.SectionStatus.ERROR -> {
                                DetailsFailContent(onTryAgainClick = listener::onRefresh)
                            }
                        }
                    }
                }
            }
        }
    }
    AppBar(
        modifier = Modifier
            .background(brush = animatedBrush)
            .windowInsetsPadding(WindowInsets.statusBars)
            .fillMaxWidth(),
        onBackButtonClicked = listener::onBackClicked,
        onShareButtonClicked = listener::onShareClicked,
        onFavoriteButtonClicked = listener::onFavoriteClicked,
        isFavorite = uiState.isFavorite
    )
}