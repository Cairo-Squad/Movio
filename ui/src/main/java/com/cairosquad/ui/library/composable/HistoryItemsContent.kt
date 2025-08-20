package com.cairosquad.ui.library.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.movio_component.SwipeToDeleteContainer
import com.cairosquad.ui.movio_component.TrendingMovieCard
import com.cairosquad.viewmodel.library.view_all_history.ViewAllHistoryInteractionListener
import com.cairosquad.viewmodel.library.view_all_history.ViewAllHistoryScreenState
import java.util.Locale

@Composable
fun HistoryItemsContent(
    movies: List<ViewAllHistoryScreenState.MovieUiState>,
    listener: ViewAllHistoryInteractionListener,
    series: List<ViewAllHistoryScreenState.SeriesUiState>
) {
    LazyColumn(
        modifier = Modifier.padding(top = 12.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
    ) {
        items(movies, key = {"${it.id} + movie"}) { movie ->
            SwipeToDeleteContainer(
                onDelete = { listener.onMovieDelete(movie.id) }
            ) {
                TrendingMovieCard(
                    modifier = Modifier.clickable {
                        listener.onMovieClick(movie.id)
                    },
                    movieTitle = movie.title,
                    rating = String.format(
                        Locale.getDefault(),
                        "%.1f",
                        movie.rating
                    ),
                    imgUrl = movie.posterPath,
                    movieCategory = if (movie.genres.isNotEmpty()) movie.genres[0] else ""
                )
            }
        }
        items(series, key = {"${it.id} + series"}) { series ->
            SwipeToDeleteContainer(
                onDelete = { listener.onSeriesDelete(series.id) }
            ) {
                TrendingMovieCard(
                    modifier = Modifier.clickable {
                        listener.onSeriesClick(series.id)
                    },
                    movieTitle = series.title,
                    rating = String.format(
                        Locale.getDefault(),
                        "%.1f",
                        series.rating
                    ),
                    imgUrl = series.posterPath,
                    movieCategory = if (series.genres.isNotEmpty()) series.genres[0] else ""
                )
            }
        }
    }
}


@Composable
fun ColumnScope.HistoryItemsEmpty() {
    Spacer(Modifier.weight(1f))
    StateMessage(
        imageDrawable =
            if (Theme.isDark) com.cairosquad.design_system.R.drawable.favorite_list_empty_dark
            else com.cairosquad.design_system.R.drawable.favorite_list_empty,
        title = stringResource(R.string.no_watch_history_yet),
        description = stringResource(R.string.start_watching_movies_and_shows_and_we_ll_keep_track_of_your_viewing_history_here)
    )
    Spacer(Modifier.weight(1f))
}
