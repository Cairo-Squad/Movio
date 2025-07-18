package com.cairosquad.ui.details

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.ExpandableText
import com.cairosquad.design_system.basic_component.InfoChip
import com.cairosquad.design_system.basic_component.SnackBar
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.movio_component.ActionBar
import com.cairosquad.ui.movio_component.ArtistCard
import com.cairosquad.ui.movio_component.LoginBottomSheet
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.ReviewCard
import com.cairosquad.ui.movio_component.SeasonCard
import com.cairosquad.ui.movio_component.SectionHeader
import com.cairosquad.ui.movio_component.ShareBottomSheet
import com.cairosquad.ui.navigation.ArtistRoute
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.ReviewsRoute
import com.cairosquad.ui.navigation.EpisodeRoute
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
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SeriesScreen(
    seriesId: Long = 1399,
    viewModel: SeriesDetailsViewModel = koinViewModel { parametersOf(seriesId) }
) {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val uiState by viewModel.screenState.collectAsStateWithLifecycle()
    val seriesUrl = "https://www.cairo-movio.com/series/${seriesId}"
    val message = stringResource(R.string.check_out_this_amazing_series)
    val encodedMessageAndUrl = Uri.encode("$message $seriesUrl")

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {

            SeriesDetailEffect.NavigateBack -> {
                navController.popBackStack()
            }

            SeriesDetailEffect.PlayTrailer -> {
                ShareUtil.playOnYoutube(videoId = "bjqEWgDVPe0", context = context)
            }

            is SeriesDetailEffect.ErrorHappened -> {
                Toast.makeText(
                    context,
                    context.getString(errorStatusToMessageResource(effect.message)),
                    Toast.LENGTH_LONG
                ).show()
                TODO("Replace it with SnackBar")
            }

            is SeriesDetailEffect.NavigateToAllArtists -> {
                navController.navigate(TopCastRoute(effect.seriesId, isMovie = false))
            }

            is SeriesDetailEffect.NavigateToAllReviews -> {
                navController.navigate(ReviewsRoute(effect.seriesId, isMovie = false))
            }

            is SeriesDetailEffect.NavigateToAllSeasons -> {
                navController.navigate(SeasonsRoute(effect.seriesId))
            }

            is SeriesDetailEffect.NavigateToAllSimilar -> {
                navController.navigate(SimilarSeriesRoute(effect.seriesId))
            }

            is SeriesDetailEffect.NavigateToArtistDetails -> {
                navController.navigate(ArtistRoute(effect.artistId))
            }

            is SeriesDetailEffect.NavigateToSeasonDetails -> {
                navController.navigate(
                    EpisodeRoute(
                        episodeId = effect.seriesId
                    )
                )
            }

            is SeriesDetailEffect.NavigateToSeriesDetails -> {
                navController.navigate(SeriesRoute(effect.seriesId))
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
                onLoginClick = {}
            )
        }
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(16.dp),
            visible = uiState.showSnackBar,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 600)
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight },
                animationSpec = tween(durationMillis = 600)
            )
        ) {
            SnackBar(
                imageVector = ImageVector.vectorResource(if (uiState.isProcessSuccess) R.drawable.archive_tick else R.drawable.danger),
                message = uiState.snackMessage,
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surfaces.surface)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(rememberScrollState())
    ) {
        SafeImageViewer(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .offset(y = (-20).dp),
            model = "https://image.tmdb.org/t/p/w500/${uiState.series.posterPath}",
            contentDescription = "",
            blur = 16,
            nudeThreshold = 0.001,
            nonNudeThreshold = 1.0
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .heightIn(max = 10000.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            userScrollEnabled = false
        ) {
            stickyHeader {
                AppBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onBackButtonClicked = listener::onBackClicked,
                    onShareButtonClicked = listener::onShareClicked,
                    onFavoriteButtonClicked = listener::onFavoriteClicked
                )

            }
            item {
                SafeImageViewer(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 24.dp)
                        .size(height = 260.dp, width = 200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    model = "https://image.tmdb.org/t/p/w500/${uiState.series.posterPath}",
                    contentDescription = "",
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    BasicText(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = uiState.series.title,
                        style = Theme.textStyle.headline.mediumMedium18.copy(
                            color = Theme.color.surfaces.onSurface
                        )
                    )
                    BasicText(
                        modifier = Modifier.padding(bottom = 12.dp),
                        text = uiState.series.genres.joinToString(", "),
                        style = Theme.textStyle.label.smallRegular14.copy(
                            color = Theme.color.surfaces.onSurfaceVariant
                        )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        InfoChip(
                            text = uiState.series.rating.toString(),
                            imgRes = R.drawable.review_star,
                        )
                        InfoChip(
                            text = stringResource(
                                R.string.seasons_count,
                                uiState.series.seasonsCount
                            ),
                            imgRes = R.drawable.ic_media
                        )
                        InfoChip(
                            text = uiState.series.releaseDate,
                            imgRes = R.drawable.date,
                        )
                    }
                    ActionBar(
                        onRateClicked = listener::onRateClicked,
                        onPlayClicked = listener::onPlayTrailerClicked,
                        onAddToListClicked = listener::onAddToListClicked
                    )
                }
            }
            item {
                ExpandableText(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    text = uiState.series.overview,
                    color = Theme.color.surfaces.onSurface,
                    style = Theme.textStyle.label.smallRegular14,
                    showMoreStyle = Theme.textStyle.label.smallRegular14,
                    showMoreColor = Theme.color.surfaces.onSurfaceVariant,
                    showLessColor = Theme.color.surfaces.onSurfaceVariant,
                )
            }
            item {
                SectionHeader(
                    modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
                    title = stringResource(R.string.top_cast),
                    actionText = stringResource(R.string.see_all),
                    actionIcon = ImageVector.vectorResource(R.drawable.arrow),
                    onActionClick = { listener.onSeeAllArtistsClicked(seriesId = uiState.series.id) }
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(uiState.cast) {
                        ArtistCard(
                            modifier = Modifier.clickable {
                                listener.onArtistClicked(it.id)
                            },
                            name = it.name,
                            imgUrl = "https://image.tmdb.org/t/p/w500/${it.photoPath}"
                        )
                    }
                }
            }
            item {
                SectionHeader(
                    modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
                    title = stringResource(R.string.current_seasons),
                    actionText = stringResource(R.string.see_all),
                    actionIcon = ImageVector.vectorResource(R.drawable.arrow),
                    onActionClick = { listener.onSeeAllSeasonsClicked(seriesId = uiState.series.id) }
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(uiState.seasons) { index, season ->
                        SeasonCard(
                            modifier = Modifier.width(260.dp),
                            seriesName = uiState.series.title,
                            seasonTitle = season.name,
                            seasonRate = season.rating,
                            totalNumberOfEpisodes = season.episodesCount.toString(),
                            movieImage = "https://image.tmdb.org/t/p/w500/${season.posterPath}",
                            yearOfPublish = season.airDate,
                            timeOfPublish = season.airDate,
                            currentSeason = season.number.toString(),
                            onClick = {
                                listener.onSeasonClicked(
                                    seriesId = uiState.series.id,
                                    seasonNumber = season.number
                                )
                            }
                        )
                    }
                }
            }
            item {
                SectionHeader(
                    modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
                    title = stringResource(R.string.reviews),
                    actionText = stringResource(R.string.see_all),
                    actionIcon = ImageVector.vectorResource(R.drawable.arrow),
                    onActionClick = { listener.onSeeAllReviewsClicked(seriesId = uiState.series.id) }
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.reviews) {
                        ReviewCard(
                            imgUrl = "https://image.tmdb.org/t/p/w500/${it.authorPhotoPath}",
                            movieTitle = it.author,
                            rating = it.rating.toString(),
                            reviewDate = it.date,
                            reviewText = it.description
                        )
                    }
                }
            }
            item {
                SectionHeader(
                    modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
                    title = stringResource(R.string.similar_series),
                    actionText = stringResource(R.string.see_all),
                    actionIcon = ImageVector.vectorResource(R.drawable.arrow),
                    onActionClick = { listener.onSeeAllSimilarClicked(seriesId = uiState.series.id) }
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.similarSeries) {
                        MovieCard(
                            modifier = Modifier
                                .width(124.dp)
                                .clickable {
                                    listener.onSeriesClicked(it.id)
                                },
                            imgUrl = "https://image.tmdb.org/t/p/w500/${it.posterPath}",
                            title = it.title,
                            vote = it.rating,
                            width = 124.dp,
                            aspectRatio = 0.775f,
                        )
                    }
                }
            }
        }
    }
}