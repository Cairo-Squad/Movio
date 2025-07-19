package com.cairosquad.ui.details.similar_series

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.details.similar_series.SimilarSeriesEffect
import com.cairosquad.viewmodel.details.similar_series.SimilarSeriesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SimilarSeriesScreen(
    seriesId: Long,
    navController: NavController,
    viewModel: SimilarSeriesViewModel = koinViewModel()
) {
    val state by viewModel.screenState.collectAsState()
    LaunchedEffect(seriesId) {
        viewModel.fetchSimilarSeries(seriesId)
    }
    ObserveAsEffect(viewModel.effect) {
        when (it) {
            is SimilarSeriesEffect.NavigateBack -> navController.popBackStack()

            is SimilarSeriesEffect.NavigateToSeriesDetails -> {
                navController.navigate(SeriesRoute(it.seriesId))
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        AppBar(
            title = stringResource(R.string.similar_series),
            onBackButtonClicked = { viewModel.onClickBack() },
        )
        LazyVerticalGrid(
            modifier = Modifier.padding(16.dp),
            columns = GridCells.Adaptive(minSize = 101.33.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(state.series) { series ->
                MovieCard(
                    title = series.title,
                    vote = series.rating,
                    imgUrl = series.posterUrl,
                    modifier = Modifier.clickable {
                        viewModel.onSeriesClicked(series.id)
                    },
                    width = null,
                    aspectRatio = 0.745F,
                )
            }
        }
    }
}