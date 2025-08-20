package com.cairosquad.ui.details.artist.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.LoadingMovieCard
import com.cairosquad.ui.movio_component.LoadingMovieImage
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.viewmodel.details.artist.ArtistInteractionListener
import com.cairosquad.viewmodel.details.artist.ArtistScreenState

@Composable
fun ArtistMoviesAndSeries(
    state: ArtistScreenState,
    listener: ArtistInteractionListener
) {
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
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
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