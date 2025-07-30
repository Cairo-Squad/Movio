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
import androidx.compose.runtime.collectAsState
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
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.ExpandableText
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.basic_component.SnackBar
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.BuildConfig
import com.cairosquad.ui.details.Constants.MOVIE_URL
import com.cairosquad.ui.details.composable.BasicDetails
import com.cairosquad.ui.details.composable.BasicDetailsLoading
import com.cairosquad.ui.details.composable.MovieReviewSection
import com.cairosquad.ui.details.composable.MovieTopCastSection
import com.cairosquad.ui.details.composable.SectionLoading
import com.cairosquad.ui.details.composable.SimilarMoviesSection
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
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.LoginRoute
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

@Composable
fun MovieScreen(
    movieId: Long,
) {
	val viewModel: MovieViewModel =
        hiltViewModel<MovieViewModel, MovieViewModel.Factory> { factory ->
            factory.create(movieId)
        }
    val navController = LocalNavController.current
    val context = LocalContext.current
    val state by viewModel.screenState.collectAsState()
    val movieUrl = "$MOVIE_URL${movieId}"
    val message = stringResource(R.string.check_out_this_amazing_movie)
    val encodedMessageAndUrl = Uri.encode("$message $movieUrl")


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
				).show() // TODO: Change to snack bar
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
				if (state.movie.trailerPath.isBlank()) {
					Toast.makeText(
						context,
						context.getString(com.cairosquad.ui.R.string.no_trailer_found_for_this_movie),
						Toast.LENGTH_LONG
					).show() // TODO: Change to snack bar
				} else {
					ShareUtil.playOnYoutube(videoId = state.movie.trailerPath, context = context)
				}
			}

			MovieEffect.NavigateToLogin -> {
				navController.navigate(LoginRoute)
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
		}
		AnimatedVisibility(
			visible = state.isNoAccountBottomSheetOpen,
			enter = fadeIn(),
			exit = fadeOut()
		) {
			LoginBottomSheet(
				isVisible = state.isNoAccountBottomSheetOpen,
				onDismiss = viewModel::onDismissLoginBottomSheet,
				onLoginClick = viewModel::onNavigateToLogin
			)
		}
		AnimatedVisibility(
			visible = state.isAddToListBottomSheetOpen,
			enter = fadeIn(),
			exit = fadeOut()
		) {
			ListBottomSheet(
				isVisible = state.isAddToListBottomSheetOpen,
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
				value = state.listName,
				onValueChange = viewModel::onListValueChange,
				onSubmit = { viewModel::onDismissCreateListBottomSheet },
				isMovie = true
			)
		}
		AnimatedVisibility(
			visible = state.isRateBottomSheetOpen,
			enter = fadeIn(),
			exit = fadeOut()
		) {
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
		MovieScreenState.ScreenStatus.ERROR -> {
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
					MovieScreenState.ScreenStatus.LOADING -> {}
					MovieScreenState.ScreenStatus.SUCCESS -> {
						if (uiState.movie.posterPath.isNotEmpty()) {
							Box {
								SafeImageViewer(
									modifier = Modifier
										.fillMaxWidth()
										.height(400.dp)
										.then(
											if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
												Modifier.blur(16.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
											} else {
												Modifier
											}
										)
										.offset(y = (-28).dp),
									model = BuildConfig.IMAGE_BASE_URL + uiState.movie.posterPath,
									contentDescription = "",
									blur = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) 16 else 0,
									isBlurForced = true
								)
								if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
									Box(
										modifier = Modifier
											.fillMaxWidth()
											.height(50.dp)
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
						} else {
							Box(
								modifier = Modifier
									.blur(16.dp)
									.fillMaxWidth()
									.height(400.dp)
									.offset(y = (- 28).dp),
							)
						}
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
							MovieScreenState.ScreenStatus.LOADING -> {
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
											model = BuildConfig.IMAGE_BASE_URL + uiState.movie.posterPath,
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
							MovieScreenState.ScreenStatus.LOADING -> {
								BasicDetailsLoading()
							}

							MovieScreenState.ScreenStatus.SUCCESS -> {
								BasicDetails(
									title = uiState.movie.title,
									genres = uiState.movie.genres,
									rating = uiState.movie.rating,
									releaseDate = uiState.movie.releaseDate,
									runtimeMinutes = uiState.movie.runtimeMinutes,
									onRateClicked = interactionListener::onRateItClick,
									onPlayTrailerClicked = interactionListener::onPlayClick,
									onAddToListClicked = interactionListener::onAddToListClick
								)
							}

							MovieScreenState.ScreenStatus.ERROR -> {}
						}
					}
					item {
						when (uiState.basicDetailsSectionState) {
							MovieScreenState.ScreenStatus.LOADING -> {
								LoadingMovieImage(
									modifier = Modifier
										.padding(horizontal = 16.dp)
										.fillMaxWidth()
										.height(height = 200.dp)
										.padding(bottom = 32.dp)
								)
							}

							MovieScreenState.ScreenStatus.SUCCESS -> {
								ExpandableText(
									modifier = Modifier
										.padding(horizontal = 16.dp)
										.padding(top = 16.dp),
									text = uiState.movie.overview,
									showMoreText = stringResource(R.string.read_more_with_dots_behind),
									showLessText = stringResource(R.string.read_less_with_dots_behind),
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
							MovieScreenState.ScreenStatus.LOADING -> {
								SectionLoading(
									headerName = stringResource(R.string.top_cast),
									sectionLoadingItem = {
										LoadingArtistCard()
									}
								)
							}

							MovieScreenState.ScreenStatus.SUCCESS -> {
								if (uiState.topCast.isNotEmpty()) {
									MovieTopCastSection(
										onActionClicked = {
											interactionListener.onSeeAllCastClick(
												uiState.movie.id
											)
										},
										onArtistClicked = interactionListener::onActorClick,
										cast = uiState.topCast,
									)
								}
							}

							MovieScreenState.ScreenStatus.ERROR -> {}
						}
					}
					item {
						when (uiState.reviewsSectionState) {
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
								if (uiState.reviews.isNotEmpty()) {
									MovieReviewSection(
										reviews = uiState.reviews,
										onActionClicked = {
											interactionListener.onSeeAllReviewsClick(
												uiState.movie.id
											)
										}
									)
								}
							}

							MovieScreenState.ScreenStatus.ERROR -> {}
						}
					}
					item {
						when (uiState.similarMoviesSectionState) {
							MovieScreenState.ScreenStatus.LOADING -> {
								SectionLoading(
									headerName = stringResource(R.string.similar_movies),
									sectionLoadingItem = {
										LoadingMovieCard(
											height = 160.dp
										)
									}
								)
							}

							MovieScreenState.ScreenStatus.SUCCESS -> {
								if (uiState.similarMovies.isNotEmpty()) {
									SimilarMoviesSection(
										similarMovies = uiState.similarMovies,
										onMovieClicked = interactionListener::onMovieClick,
										onActionClicked = {
											interactionListener.onSeeAllSimilarMoviesClick(
												uiState.movie.id
											)
										}
									)
								}
							}

							MovieScreenState.ScreenStatus.ERROR -> {}
						}
					}
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