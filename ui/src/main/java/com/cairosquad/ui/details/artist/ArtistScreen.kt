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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.ExpandableText
import com.cairosquad.design_system.basic_component.InfoChip
import com.cairosquad.design_system.modifier.CustomBrush
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.safe_image_viewer.safe_image_viewer.SafeImageViewer
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.ui.utils.errorStatusToMessageResource
import com.cairosquad.viewmodel.details.artist.ArtistEffect
import com.cairosquad.viewmodel.details.artist.ArtistInteractionListener
import com.cairosquad.viewmodel.details.artist.ArtistScreenState
import com.cairosquad.viewmodel.details.artist.ArtistViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ArtistScreen(
    artistId: Long,
    navController: NavController,
    modifier: Modifier = Modifier,
    artistViewModel: ArtistViewModel = koinViewModel()

) {

    LaunchedEffect(Unit) {
        artistViewModel.loadArtistDetails(artistId)
        artistViewModel.loadArtistMovies(artistId)
        artistViewModel.loadArtistSeries(artistId)
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
    ) {


        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(340.dp)
        ) {
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .align(Alignment.TopStart)
                    .background(
                        brush = verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 1f),
                                Color.Black.copy(alpha = 0f)
                            )
                        )
                    )
            )
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

            AppBar(
                onBackButtonClicked = listener::onClickBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(top = 4.dp)
            )

            SafeImageViewer(
                model = "https://image.tmdb.org/t/p/w500${state.artist.photoPath}",
                modifier = Modifier
                    .padding(horizontal = 6.67.dp)
                    .padding(top = 31.dp)
                    .size(160.dp)
                    .clip(CircleShape),
                contentDescription = stringResource(R.string.artist_image),
                nudeThreshold = 0.0,
                nonNudeThreshold = 0.0
            )
        }

        BasicText(
            modifier = Modifier.padding(start = 16.dp),
            text = state.artist.name,
            style = Theme.textStyle.headline.mediumMedium18
                .copy(color = Theme.color.surfaces.onSurface),
        )

        BasicText(
            modifier = Modifier.padding(start = 16.dp, top = 4.dp),
            text = state.artist.department,
            style = Theme.textStyle.label.smallRegular14
                .copy(color = Theme.color.surfaces.onSurfaceVariant),
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        ) {
            InfoChip(
                text = formatBirthDateLegacy(state.artist.birthDate),
                imgRes = R.drawable.date,
            )
            InfoChip(
                text = state.artist.country,
                imgRes = R.drawable.component_1,
            )
        }

        ExpandableText(
            text = state.artist.biography,
            color = Theme.color.surfaces.onSurface,
            style = Theme.textStyle.label.smallRegular14,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .padding(horizontal = 16.dp),
            collapsedMaxLine = 5,
            showMoreColor = Theme.color.surfaces.onSurfaceVariant
        )

        Text(
            text = "Known For",
            style = Theme.textStyle.title.mediumMedium16,
            color = Theme.color.surfaces.onSurface,
            modifier = Modifier.padding(start = 16.dp, top = 32.dp, bottom = 12.dp)
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

            items(state.KnownForSeries) { series ->
                MovieCard(
                    title = series.title,
                    vote = series.rating,
                    imgUrl = series.posterPath,
                    width = 124.dp,
                    aspectRatio = 0.67f,
                    modifier = Modifier.clickable { listener.onMovieClick(series.id) }
                )
            }
        }

    }
}

private fun formatBirthDateLegacy(birthDateLong: Long): String {
    val date = Date(birthDateLong)
    val formatter = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
    return formatter.format(date)
}