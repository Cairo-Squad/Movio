package com.cairosquad.ui.details

import android.content.ActivityNotFoundException
import android.content.Intent
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.ExpandableText
import com.cairosquad.design_system.basic_component.InfoChip
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.movio_component.ActionBar
import com.cairosquad.ui.movio_component.ArtistCard
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.ReviewCard
import com.cairosquad.ui.movio_component.SectionHeader
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.details.series.SeriesDetailEffect
import com.cairosquad.viewmodel.details.series.SeriesDetailsInteractionListener
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState
import com.cairosquad.viewmodel.details.series.SeriesDetailsViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SeriesScreen(
    seriesId: Long,
    viewModel: SeriesDetailsViewModel = koinViewModel { parametersOf(seriesId) }
) {
//    val navController = LocalNavController.current
    val context = LocalContext.current
    val uiState by viewModel.screenState.collectAsStateWithLifecycle()

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            is SeriesDetailEffect.RateSeries -> {

            }

            is SeriesDetailEffect.ShareSeries -> {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain" // MIME type for text
                    putExtra(Intent.EXTRA_SUBJECT, "Check this out!")
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Hey! Watch this amazing video: https://www.cairo-movio/series/${uiState.series.id}"
                    )
                }

                context.startActivity(Intent.createChooser(shareIntent, "Share via"))
            }

            is SeriesDetailEffect.ErrorHappened -> {

            }

            is SeriesDetailEffect.FavoriteSeries -> {

            }

            is SeriesDetailEffect.NavigateBack -> {

            }

            is SeriesDetailEffect.PlayTrailer -> {
                val videoId = "dQw4w9WgXcQ"
                val appIntent = Intent(Intent.ACTION_VIEW, "vnd.youtube:$videoId".toUri())
                val webIntent = Intent(
                    Intent.ACTION_VIEW,
                    "https://www.youtube.com/watch?v=$videoId".toUri()
                )

                try {
                    context.startActivity(appIntent)
                } catch (_: ActivityNotFoundException) {
                    context.startActivity(webIntent)
                }
            }

            is SeriesDetailEffect.AddSeriesToList -> {

            }

            is SeriesDetailEffect.NavigateToAllArtists -> {

            }

            is SeriesDetailEffect.NavigateToAllReviews -> {

            }

            is SeriesDetailEffect.NavigateToAllSeasons -> {

            }

            is SeriesDetailEffect.NavigateToAllSimilar -> {

            }

            is SeriesDetailEffect.NavigateToArtistDetails -> {

            }

            is SeriesDetailEffect.NavigateToSeasonDetails -> {

            }

            is SeriesDetailEffect.NavigateToSeriesDetails -> {

            }

            is SeriesDetailEffect.NavigateToTrailer -> {

            }
        }
    }

    SeriesScreenContent(
        uiState = uiState,
        listener = viewModel
    )
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
                .blur(16.dp)
                .offset(y = (-20).dp),
            model = "https://image.tmdb.org/t/p/w500/${uiState.series.posterPath}",
            contentDescription = "",
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
                            imgRes = R.drawable.review_star
                        )
//                        InfoChip(text = "7 seasons", imgRes = R.drawable.)
                        InfoChip(
                            text = uiState.series.releaseDate,
                            imgRes = R.drawable.date
                        )
                    }
                    ActionBar()
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
                    title = "Top Cast",
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
                    title = "Current Seasons",
                    actionText = stringResource(R.string.see_all),
                    actionIcon = ImageVector.vectorResource(R.drawable.arrow),
                    onActionClick = { listener.onSeeAllSeasonsClicked(seriesId = uiState.series.id) }
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.seasons) {
                        ArtistCard(
                            modifier = Modifier.clickable {
                                listener.onSeasonClicked(
                                    seriesId = uiState.series.id,
                                    seasonNumber = it.number
                                )
                            },
                            name = it.name,
                            imgUrl = "https://image.tmdb.org/t/p/w500/${it.posterPath}"
                        )
                    }
                }
            }
            item {
                SectionHeader(
                    modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
                    title = "Reviews",
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
                    title = "Similar Series",
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
                            aspectRatio = 0.775f
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SeriesScreenPreview() {
    MovioTheme(isDarkTheme = true) {
        SeriesScreen(123)
    }
}