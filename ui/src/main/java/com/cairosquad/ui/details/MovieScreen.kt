package com.cairosquad.ui.details

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.ExpandableText
import com.cairosquad.design_system.basic_component.InfoChip
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.movio_component.ActionBar
import com.cairosquad.ui.movio_component.ArtistCard
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.ReviewCard
import com.cairosquad.ui.movio_component.SectionHeader
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.viewmodel.details.movie.MovieInteractionListener
import com.cairosquad.viewmodel.details.movie.MovieScreenState
import com.cairosquad.viewmodel.details.movie.MovieViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieScreen(
    movieId: Long,
    viewModel: MovieViewModel = koinViewModel()
) {
    val navController = LocalNavController.current

    val state by viewModel.screenState.collectAsState()



}

@Composable
fun MovieContent(
    uiState: MovieScreenState,
    interactionListener: MovieInteractionListener,
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
            model = "https://image.tmdb.org/t/p/w500/${uiState.movie.posterPath}",
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
                    onBackButtonClicked = interactionListener::onBackClick,
                    onShareButtonClicked = interactionListener::onShareClick,
                    onFavoriteButtonClicked = interactionListener::onFavoriteClick
                )

            }
            item {
                SafeImageViewer(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 24.dp)
                        .size(height = 260.dp, width = 200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    model = "https://image.tmdb.org/t/p/w500/${uiState.movie.posterPath}",
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
                        text = uiState.movie.title,
                        style = Theme.textStyle.headline.mediumMedium18.copy(
                            color = Theme.color.surfaces.onSurface
                        )
                    )
                    BasicText(
                        modifier = Modifier.padding(bottom = 12.dp),
                        text = uiState.movie.genres.joinToString(", "),
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
                            text = uiState.movie.rating.toString(),
                            imgRes = R.drawable.review_star,
                        )

                        InfoChip(
                            text = uiState.movie.releaseDate,
                            imgRes = R.drawable.date,
                        )
                    }
                    ActionBar(
                        onRateClicked = interactionListener::onRateItClick,
                        onPlayClicked = interactionListener::onPlayClick,
                        onAddToListClicked = interactionListener::onAddToListClick
                    )
                }
            }
            item {
                ExpandableText(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    text = uiState.movie.overview,
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
                    onActionClick = { interactionListener.onSeeAllCastClick() }
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(uiState.topCast) {
                        ArtistCard(
                            modifier = Modifier.clickable {
                                interactionListener.onActorClick(it.id)
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
                    title = "Reviews",
                    actionText = stringResource(R.string.see_all),
                    actionIcon = ImageVector.vectorResource(R.drawable.arrow),
                    onActionClick = { interactionListener.onSeeAllReviewsClick() }
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
                    onActionClick = { interactionListener.onSeeAllSimilarMoviesClick() }
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.similarMovies) {
                        MovieCard(
                            modifier = Modifier
                                .width(124.dp)
                                .clickable {
                                    interactionListener.onMovieClick(it.id)
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

