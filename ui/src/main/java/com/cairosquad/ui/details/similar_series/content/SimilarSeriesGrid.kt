package com.cairosquad.ui.details.similar_series.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cairosquad.ui.details.DetailsFailContent
import com.cairosquad.ui.movio_component.LoadingMovieCard
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.viewmodel.details.similar_series.SimilarSeriesScreenState
import com.cairosquad.viewmodel.details.similar_series.SimilarSeriesViewModel

@Composable
fun SimilarSeriesGrid(
    state: SimilarSeriesScreenState,
    viewModel: SimilarSeriesViewModel,
    seriesId: Long
) {
    LazyVerticalGrid(
        modifier = Modifier.padding(horizontal = 16.dp),
        columns = GridCells.Adaptive(minSize = 101.33.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        when (state.screenStatus) {
            SimilarSeriesScreenState.ScreenStatus.LOADING -> {
                items(20) {
                    LoadingMovieCard()
                }
            }

            SimilarSeriesScreenState.ScreenStatus.SUCCESS -> {
                items(state.series) { series ->
                    MovieCard(
                        title = series.title,
                        vote = series.rating,
                        imgUrl = series.posterUrl,
                        modifier = Modifier.clickable {
                            viewModel.onSeriesClick(series.id)
                        },
                        width = null,
                        aspectRatio = 0.745F,
                    )
                }
            }

            SimilarSeriesScreenState.ScreenStatus.ERROR -> {
                item { DetailsFailContent(onTryAgainClick = { viewModel.onRefresh(seriesId) }) }
            }
        }
    }
}