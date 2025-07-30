package com.cairosquad.ui.details.artist

import android.widget.Toast
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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.navigation.NavController
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.ExpandableText
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.basic_component.InfoChip
import com.cairosquad.design_system.modifier.CustomBrush
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.BuildConfig
import com.cairosquad.ui.movio_component.LoadingMovieCard
import com.cairosquad.ui.movio_component.LoadingMovieImage
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.ui.utils.errorStatusToMessageResource
import com.cairosquad.viewmodel.details.artist.ArtistEffect
import com.cairosquad.viewmodel.details.artist.ArtistInteractionListener
import com.cairosquad.viewmodel.details.artist.ArtistScreenState
import com.cairosquad.viewmodel.details.artist.ArtistViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ArtistScreen(
    artistId: Long,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val artistViewModel: ArtistViewModel =
        hiltViewModel<ArtistViewModel, ArtistViewModel.Factory> { factory ->
            factory.create(artistId)
        }

    val state by artistViewModel.screenState.collectAsState()
    val context = LocalContext.current
    ObserveAsEffect(artistViewModel.effect) { effect ->
        when (effect) {
            is ArtistEffect.ErrorHappened -> {
                Toast.makeText(
                    context,
                    context.getString(errorStatusToMessageResource(effect.message)),
                    Toast.LENGTH_LONG
                ).show()
            }

            is ArtistEffect.NavigateToMovieDetails -> {
                navController.navigate(MovieRoute(effect.movieId))
            }

            is ArtistEffect.NavigateBack -> {
                navController.popBackStack()
            }

            is ArtistEffect.NavigateToSeriesDetails -> {
                navController.navigate(SeriesRoute(effect.seriesId))
            }
        }
    }

    ArtistScreenContent(modifier = modifier, state = state, listener = artistViewModel)
}

@Composable
private fun ArtistScreenContent(
    modifier: Modifier = Modifier,
    state: ArtistScreenState,
    listener: ArtistInteractionListener
) {
    val listScroll = rememberScrollState()
    val density = LocalDensity.current
    val scrollThresholdPx = with(density) { 250.dp.toPx() }
    val progress = (listScroll.value / scrollThresholdPx).coerceIn(0f, 1f)

    val animatedStartColor =
        lerp(Theme.color.surfaces.statusBarShadow, Theme.color.surfaces.surface, progress)
    val animatedEndColor = lerp(Color.Transparent, Theme.color.surfaces.surface, progress)
    val animatedBrush = verticalGradient(colors = listOf(animatedStartColor, animatedEndColor))
    Box(modifier = modifier.fillMaxSize()) {
        if (state.screenStatus == ArtistScreenState.ScreenStatus.FAILED) {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                StateMessage(
                    imageDrawable = R.drawable.no_internet,
                    titleId = R.string.no_internet_connection,
                    descriptionId = R.string.internet_is_not_available_description
                )
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .verticalScroll(listScroll),
                horizontalAlignment = Alignment.Start,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                ) {
                    if (state.artist.photoPath.isBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .height(335.dp)
                                .offset(y = (-5).dp)
                                .CustomBrush(0.5f, 16.dp),
                        )
                    } else {
                        SafeImageViewer(
                            model = "https://image.tmdb.org/t/p/w500${state.artist.photoPath}",
                            contentDescription = "blured image",
                            modifier = Modifier
                                .fillMaxSize()
                                .height(335.dp)
                                .offset(y = (-5).dp)
                                .CustomBrush(0.5f, 16.dp),
                            nudeThreshold = 0.0,
                            nonNudeThreshold = 0.0
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                brush = verticalGradient(
                                    colors = listOf(
                                        Theme.color.surfaces.surface.copy(alpha = 0f),
                                        Theme.color.surfaces.surface.copy(alpha = .10f),
                                        Theme.color.surfaces.surface.copy(alpha = .50f),
                                        Theme.color.surfaces.surface.copy(alpha = .90f),
                                        Theme.color.surfaces.surface,
                                    )
                                )
                            )
                    )
                    when (state.screenStatus) {
                        ArtistScreenState.ScreenStatus.LOADING -> {
                            LoadingMovieImage(
                                Modifier
                                    .padding(top = 31.dp)
                                    .clip(CircleShape)
                                    .size(160.dp)
                            )
                        }

                        ArtistScreenState.ScreenStatus.SUCCESS -> {
                            if (state.artist.photoPath.isNotEmpty()) {
                                SafeImageViewer(
                                    model = BuildConfig.IMAGE_BASE_URL + state.artist.photoPath,
                                    modifier = Modifier
                                        .padding(horizontal = 6.67.dp)
                                        .padding(top = 31.dp)
                                        .size(160.dp)
                                        .clip(CircleShape),
                                    contentDescription = stringResource(R.string.artist_image),
                                    nudeThreshold = 0.0,
                                    nonNudeThreshold = 0.0,
                                    loadingPlaceholder = {
                                        LoadingMovieImage(
                                            Modifier
                                                .clip(CircleShape)
                                                .size(160.dp)
                                        )
                                    }
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 6.67.dp)
                                        .padding(top = 31.dp)
                                        .size(160.dp)
                                        .clip(CircleShape)
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

                        ArtistScreenState.ScreenStatus.FAILED -> {}
                    }
                }
                when (state.screenStatus) {
                    ArtistScreenState.ScreenStatus.LOADING -> {
                        LoadingMovieImage(
                            Modifier
                                .padding(horizontal = 16.dp)
                                .clip(CircleShape)
                                .size(width = 60.dp, height = 32.dp)
                        )
                    }

                    ArtistScreenState.ScreenStatus.SUCCESS -> {
                        BasicText(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = state.artist.name,
                            style = Theme.textStyle.headline.mediumMedium18
                                .copy(color = Theme.color.surfaces.onSurface),
                        )
                    }

                    ArtistScreenState.ScreenStatus.FAILED -> {}
                }
                when (state.screenStatus) {
                    ArtistScreenState.ScreenStatus.LOADING -> {
                        LoadingMovieImage(
                            Modifier
                                .padding(start = 16.dp, top = 4.dp)
                                .clip(CircleShape)
                                .size(width = 60.dp, height = 32.dp)
                        )
                    }

                    ArtistScreenState.ScreenStatus.SUCCESS -> {
                        BasicText(
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                            text = state.artist.department,
                            style = Theme.textStyle.label.smallRegular14
                                .copy(color = Theme.color.surfaces.onSurfaceVariant),
                        )
                    }

                    ArtistScreenState.ScreenStatus.FAILED -> {}
                }
                when (state.screenStatus) {
                    ArtistScreenState.ScreenStatus.LOADING -> {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            repeat(2) {
                                LoadingMovieImage(
                                    Modifier
                                        .padding(top = 16.dp, start = 16.dp)
                                        .clip(CircleShape)
                                        .size(width = 60.dp, height = 32.dp)
                                )
                            }
                        }
                    }

					ArtistScreenState.ScreenStatus.SUCCESS -> {
						LazyRow(
							horizontalArrangement = Arrangement.spacedBy(8.dp),
							modifier = Modifier.padding(top = 16.dp),
							contentPadding = PaddingValues(horizontal = 16.dp)
						) {
							val lastWord = state.artist.country
								.trim()
								.let { full ->
									val partsByComma = full.split(",")
									val lastPart = partsByComma.lastOrNull()?.trim().orEmpty()
									lastPart.split(" ").lastOrNull()?.trim()
								}
								?.takeIf { it.isNotBlank() }
							state.artist.birthDate?.let{ birthDate ->
								item {
									InfoChip(
										text = formatBirthDateLegacy(birthDate),
										imgRes = R.drawable.date,
									)
								}
							}
							if (lastWord != null) {
								item {
									InfoChip(
										text = state.artist.country,
										imgRes = R.drawable.component_1,
									)
								}
							}
						}
					}

                    ArtistScreenState.ScreenStatus.FAILED -> {}
                }
                when (state.screenStatus) {
                    ArtistScreenState.ScreenStatus.LOADING -> {
                        LoadingMovieImage(
                            Modifier
                                .padding(16.dp)
                                .height(120.dp)
                                .fillMaxWidth()
                        )
                    }

                    ArtistScreenState.ScreenStatus.SUCCESS -> {
                        if (state.artist.biography.isNotBlank()) {
                            ExpandableText(
                                text = state.artist.biography,
                                color = Theme.color.surfaces.onSurface,
                                style = Theme.textStyle.label.smallRegular14,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                collapsedMaxLine = 5,
                                showMoreText = stringResource(com.cairosquad.ui.R.string.read_more_with_dotes_behind),
                                showMoreColor = Theme.color.surfaces.onSurfaceVariant,
                                showLessText = stringResource(com.cairosquad.ui.R.string.read_less_with_dotes_behind)
                            )
                        }
                    }

                    ArtistScreenState.ScreenStatus.FAILED -> {}
                }
                when (state.screenStatus) {
                    ArtistScreenState.ScreenStatus.LOADING -> {
                        LoadingMovieImage(
                            Modifier.size(height = 32.dp, width = 64.dp)
                        )
                        LazyRow {
                            items(10) {
                                LoadingMovieCard()
                            }
                        }
                    }

					ArtistScreenState.ScreenStatus.SUCCESS -> {
						if (state.knownForMovies.isNotEmpty() || state.knownForSeries.isNotEmpty()) {
							BasicText(
								modifier = Modifier.padding(
									start = 16.dp,
									top = 32.dp,
									bottom = 12.dp
								),
								text = stringResource(R.string.known_for),
								style = Theme.textStyle.title.mediumMedium16
									.copy(color = Theme.color.surfaces.onSurface)
							)
							LazyRow(
								horizontalArrangement = Arrangement.spacedBy(12.dp),
								contentPadding = PaddingValues(horizontal = 16.dp)
							) {
								items(state.knownForMovies) { movie ->
									MovieCard(
										title = movie.title,
										vote = movie.rating,
										imgUrl = movie.posterPath,
										width = 124.dp,
										aspectRatio = 0.67f,
										modifier = Modifier.clickable { listener.onMovieClick(movie.id) }
									)
								}
								items(state.knownForSeries) { series ->
									MovieCard(
										title = series.title,
										vote = series.rating,
										imgUrl = series.posterPath,
										width = 124.dp,
										aspectRatio = 0.67f,
										modifier = Modifier.clickable { listener.onSeriesClick(series.id) }
									)
								}
							}
						}
					}

                    ArtistScreenState.ScreenStatus.FAILED -> {}
                }
            }
        }
        AppBar(
            onBackButtonClicked = listener::onClickBack,
            modifier = Modifier
                .background(animatedBrush)
                .windowInsetsPadding(WindowInsets.statusBars)
        )
    }
}

private fun formatBirthDateLegacy(birthDateLong: Long): String {
    val date = Date(birthDateLong)
    val formatter = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
    return formatter.format(date)
}