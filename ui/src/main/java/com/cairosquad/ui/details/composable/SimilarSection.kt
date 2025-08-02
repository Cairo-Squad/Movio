package com.cairosquad.ui.details.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.ui.BuildConfig
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.SectionHeader
import com.cairosquad.viewmodel.details.movie.MovieScreenState
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState

@Composable
fun SimilarSeriesSection(
    similarSeries: List<SeriesDetailsScreenState.SeriesUiState>,
    onSeriesClicked: (Long) -> Unit,
    onActionClicked: () -> Unit
) {
    SectionHeader(
        modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
        title = stringResource(R.string.similar_series),
        actionText = stringResource(R.string.see_all),
        actionIcon = ImageVector.vectorResource(R.drawable.arrow),
        onActionClick = onActionClicked
    )
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
       modifier = Modifier.padding(bottom = 32.dp)
    ) {
        items(similarSeries) {
            MovieCard(
                modifier = Modifier
                    .width(124.dp)
                    .clickable {
                        onSeriesClicked(it.id)
                    },
                imgUrl = BuildConfig.IMAGE_BASE_URL + it.posterPath,
                title = it.title,
                vote = it.rating,
                width = 124.dp,
                aspectRatio = 0.775f,
            )
        }
    }
}

@Composable
fun SimilarMoviesSection(
    similarMovies: List<MovieScreenState.MovieDetailsUiState>,
    onMovieClicked: (Long) -> Unit,
    onActionClicked: () -> Unit
) {
    SectionHeader(
        modifier = Modifier.padding(top = 32.dp, bottom = 12.dp),
        title = stringResource(R.string.similar_movies),
        actionText = stringResource(R.string.see_all),
        actionIcon = ImageVector.vectorResource(R.drawable.arrow),
        onActionClick = onActionClicked
    )
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        modifier = Modifier.padding(bottom = 32.dp)
    ) {
        items(similarMovies) {
            MovieCard(
                modifier = Modifier
                    .width(124.dp)
                    .clickable {
                        onMovieClicked(it.id)
                    },
                imgUrl = BuildConfig.IMAGE_BASE_URL + it.posterPath,
                title = it.title,
                vote = it.rating,
                width = 124.dp,
                aspectRatio = 0.775f,
            )
        }
    }
}