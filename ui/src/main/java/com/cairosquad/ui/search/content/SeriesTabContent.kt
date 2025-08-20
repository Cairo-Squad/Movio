package com.cairosquad.ui.search.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.search.composable.SearchResultText
import com.cairosquad.viewmodel.search.SearchInteractionListener
import com.cairosquad.viewmodel.search.SearchScreenState

@Composable
fun SeriesTabContent(
    series: LazyPagingItems<SearchScreenState.SeriesUiState>,
    state: SearchScreenState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier
) {
    val isLoading = series.loadState.refresh is LoadState.Loading
    val isError = series.loadState.refresh is LoadState.Error
    val isEmpty = series.itemCount == 0 && !isLoading && !isError
    val error = (series.loadState.refresh as? LoadState.Error)?.error

    LaunchedEffect(error) {
        if (error != null) {
            listener.onTabPagingError(error)
        }
    }

    when {
        isLoading -> {
            SearchLoadingContent(
                modifier = modifier
            )
        }

        isError -> {
            SearchResultFail(
                errorStatus = state.errorStatus,
                listener
            )
        }

        isEmpty -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                StateMessage(
                    imageDrawable =
                        if (Theme.isDark) R.drawable.no_result_dark
                        else R.drawable.no_result,
                    titleId = R.string.no_results_found,
                    descriptionId = R.string.no_search_results_found_description
                )
            }
        }

        else -> {
            LazyVerticalGrid(
                modifier = modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 101.33.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SearchResultText(noOfResults = series.itemCount)
                }
                items(series.itemCount) { index ->
                    series[index]?.let { series ->
                        MovieCard(
                            modifier = Modifier
                                .clickable(onClick = { listener.onSeriesClick(series.id) }),
                            title = series.title,
                            vote = series.rating,
                            imgUrl = series.posterPath,
                            width = null,
                            aspectRatio = 0.743f
                        )
                    }
                }
            }
        }
    }
}