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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.ExpandableText
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.basic_component.InfoChip
import com.cairosquad.design_system.basic_component.SnackBar
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.movio_component.ActionBar
import com.cairosquad.ui.movio_component.ArtistCard
import com.cairosquad.ui.movio_component.LoginBottomSheet
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.ReviewCard
import com.cairosquad.ui.movio_component.SectionHeader
import com.cairosquad.ui.movio_component.ShareBottomSheet
import com.cairosquad.ui.navigation.ArtistRoute
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.ReviewsRoute
import com.cairosquad.ui.navigation.SimilarMovieRoute
import com.cairosquad.ui.navigation.TopCastRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.ui.utils.ShareUtil
import com.cairosquad.ui.utils.errorStatusToMessageResource
import com.cairosquad.viewmodel.details.movie.MovieEffect
import com.cairosquad.viewmodel.details.movie.MovieInteractionListener
import com.cairosquad.viewmodel.details.movie.MovieScreenState
import com.cairosquad.viewmodel.details.movie.MovieViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MovieScreen(
    movieId: Long,
    viewModel: MovieViewModel = koinViewModel { parametersOf(movieId) }
) {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val state by viewModel.screenState.collectAsState()
    val seriesUrl = "https://www.cairo-movio.com/movie/${movieId}"
    val message = stringResource(R.string.check_out_this_amazing_movie)
    val encodedMessageAndUrl = Uri.encode("$message $seriesUrl")


    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            is MovieEffect.NavigateToActor -> {
                navController.navigate(ArtistRoute(effect.actorId))
            }

            is MovieEffect.NavigateToMovie -> {
                navController.navigate(MovieRoute(effect.movieId))
            }

            is MovieEffect.ErrorHappened -> {
                Toast.makeText(
                    context,
                    context.getString(errorStatusToMessageResource(effect.message)),
                    Toast.LENGTH_LONG
                ).show()
            }

            MovieEffect.NavigateBack -> {
                navController.popBackStack()
            }

            is MovieEffect.NavigateToAllActors -> {
                navController.navigate(TopCastRoute(movieId, isMovie = true))
            }

            is MovieEffect.NavigateToAllReviews -> {
                navController.navigate(ReviewsRoute(movieId, isMovie = true))
            }

            is MovieEffect.NavigateToSimilarMovies -> {
                navController.navigate(SimilarMovieRoute(movieId))
            }

            MovieEffect.PlayTrailer -> {
                ShareUtil.playOnYoutube(videoId = state.movie.trailerPath, context = context)
            }
        }
    }
    Box {
        MovieContent(
            uiState = state,
            interactionListener = viewModel
        )
        AnimatedVisibility(
            visible = state.isShareBottomSheetOpen,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ShareBottomSheet(
                isVisible = state.isShareBottomSheetOpen,
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
            visible = state.isNoAccountBottomSheetOpen,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LoginBottomSheet(
                isVisible = state.isNoAccountBottomSheetOpen,
                onDismiss = viewModel::onDismissLoginBottomSheet,
                onLoginClick = {}
            )
        }
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(16.dp),
            visible = state.showSnackBar,
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
                imageVector = ImageVector.vectorResource(if (state.isProcessSuccess) R.drawable.archive_tick else R.drawable.danger),
                message = state.snackMessage,
                action = {}
            )
        }
    }
}

@Composable
fun MovieContent(
    uiState: MovieScreenState,
    interactionListener: MovieInteractionListener,
) {
    val listState = rememberScrollState()
    val density = LocalDensity.current

    val scrollThresholdPx = with(density) { 275.dp.toPx() }

    val progress = (listState.value / scrollThresholdPx).coerceIn(0f, 1f)

    val animatedStartColor = lerp(
        start = Color.Black,
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surfaces.surface)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .verticalScroll(listState)
    ) {
        when (uiState.basicDetailsSectionState) {
            MovieScreenState.ScreenStatus.INITIAL -> {}
            MovieScreenState.ScreenStatus.LOADING -> {}
            MovieScreenState.ScreenStatus.SUCCESS -> {
                SafeImageViewer(
                    modifier = Modifier
                        .blur(16.dp)
                        .fillMaxWidth()
                        .height(400.dp)
                        .offset(y = (-28).dp),
                    model = "https://image.tmdb.org/t/p/w500/${uiState.movie.posterPath}",
                    contentDescription = "",
                    blur = 0,
                    nudeThreshold = 0.0,
                    nonNudeThreshold = 0.0
                )
            }

            MovieScreenState.ScreenStatus.ERROR -> {}
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .heightIn(max = 10000.dp),
            horizontalAlignment = Alignment.Start,
            userScrollEnabled = false
        ) {
            item {
                when (uiState.basicDetailsSectionState) {
                    MovieScreenState.ScreenStatus.INITIAL -> {}
                    MovieScreenState.ScreenStatus.LOADING -> {}
                    MovieScreenState.ScreenStatus.SUCCESS -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 56.dp, bottom = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (uiState.movie.posterPath.isNotEmpty()) {
                                SafeImageViewer(
                                    modifier = Modifier
                                        .size(height = 260.dp, width = 200.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    model = "https://image.tmdb.org/t/p/w500/${uiState.movie.posterPath}",
                                    contentDescription = "",
                                )
                            } else {
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
                    }

                    MovieScreenState.ScreenStatus.ERROR -> {}
                }
            }
            item {
                when (uiState.basicDetailsSectionState) {
                    MovieScreenState.ScreenStatus.INITIAL -> {}
                    MovieScreenState.ScreenStatus.LOADING -> {}
                    MovieScreenState.ScreenStatus.SUCCESS -> {
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
                                    text = uiState.movie.runtimeMinutes,
                                    imgRes = R.drawable.time,
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

                    MovieScreenState.ScreenStatus.ERROR -> {}
                }
            }
            item {
                when (uiState.basicDetailsSectionState) {
                    MovieScreenState.ScreenStatus.INITIAL -> {}
                    MovieScreenState.ScreenStatus.LOADING -> {}
                    MovieScreenState.ScreenStatus.SUCCESS -> {
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

                    MovieScreenState.ScreenStatus.ERROR -> {}
                }
            }
            item {
                when (uiState.castSectionState) {
                    MovieScreenState.ScreenStatus.INITIAL -> {}
                    MovieScreenState.ScreenStatus.LOADING -> {}
                    MovieScreenState.ScreenStatus.SUCCESS -> {
                        SectionHeader(
                            modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
                            title = "Top Cast",
                            actionText = stringResource(R.string.see_all),
                            actionIcon = ImageVector.vectorResource(R.drawable.arrow),
                            onActionClick = { interactionListener.onSeeAllCastClick(uiState.movie.id) }
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

                    MovieScreenState.ScreenStatus.ERROR -> {}
                }
            }
            item {
                when (uiState.reviewsSectionState) {
                    MovieScreenState.ScreenStatus.INITIAL -> {}
                    MovieScreenState.ScreenStatus.LOADING -> {}
                    MovieScreenState.ScreenStatus.SUCCESS -> {
                        SectionHeader(
                            modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
                            title = "Reviews",
                            actionText = stringResource(R.string.see_all),
                            actionIcon = ImageVector.vectorResource(R.drawable.arrow),
                            onActionClick = { interactionListener.onSeeAllReviewsClick(uiState.movie.id) }
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.reviews) {
                                ReviewCard(
                                    imgUrl = "https://image.tmdb.org/t/p/w500/${it.authorPhotoPath}",
                                    reviewerName = it.author,
                                    rating = it.rating.toString(),
                                    reviewDate = it.date,
                                    reviewText = it.description,
                                    isExpandable = false
                                )
                            }
                        }
                    }

                    MovieScreenState.ScreenStatus.ERROR -> {}
                }
            }
            item {
                when (uiState.similarMoviesSectionState) {
                    MovieScreenState.ScreenStatus.INITIAL -> {}
                    MovieScreenState.ScreenStatus.LOADING -> {}
                    MovieScreenState.ScreenStatus.SUCCESS -> {
                        SectionHeader(
                            modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
                            title = "Similar Movies",
                            actionText = stringResource(R.string.see_all),
                            actionIcon = ImageVector.vectorResource(R.drawable.arrow),
                            onActionClick = { interactionListener.onSeeAllSimilarMoviesClick(uiState.movie.id) }
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

                    MovieScreenState.ScreenStatus.ERROR -> {}
                }
            }
        }
    }
    AppBar(
        modifier = Modifier
            .background(animatedBrush)
            .windowInsetsPadding(WindowInsets.statusBars)
            .fillMaxWidth(),
        onBackButtonClicked = interactionListener::onBackClick,
        onShareButtonClicked = interactionListener::onShareClick,
        onFavoriteButtonClicked = interactionListener::onFavoriteClick
    )
}