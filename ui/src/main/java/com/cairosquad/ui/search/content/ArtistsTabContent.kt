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
import com.cairosquad.ui.movio_component.ArtistCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.search.composable.SearchResultText
import com.cairosquad.viewmodel.search.SearchInteractionListener
import com.cairosquad.viewmodel.search.SearchScreenState

@Composable
fun ArtistsTabContent(
    artist: LazyPagingItems<SearchScreenState.ArtistUiState>,
    state: SearchScreenState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier
) {
    val isLoading = artist.loadState.refresh is LoadState.Loading
    val isError = artist.loadState.refresh is LoadState.Error
    val isEmpty = artist.itemCount == 0 && !isLoading && !isError
    val error = (artist.loadState.refresh as? LoadState.Error)?.error

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
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    SearchResultText(noOfResults = artist.itemCount)
                }
                items(artist.itemCount) { index ->
                    artist[index]?.let { artist ->
                        ArtistCard(
                            modifier = Modifier
                                .clickable(onClick = { listener.onArtistClick(artist.id) }),
                            name = artist.name,
                            imgUrl = artist.photoPath,
                        )
                    }
                }
            }
        }
    }
}