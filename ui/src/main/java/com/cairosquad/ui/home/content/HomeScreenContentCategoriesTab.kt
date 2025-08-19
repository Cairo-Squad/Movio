package com.cairosquad.ui.home.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.CategoriesChips
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.viewmodel.home.HomeInteractionsListener
import com.cairosquad.viewmodel.home.HomeScreenState
import com.cairosquad.viewmodel.home.HomeScreenState.SortingType

@Composable
fun HomeScreenContentCategoriesTab(
    screenState: HomeScreenState,
    listener: HomeInteractionsListener,
    lazyGridState: LazyGridState,
    modifier: Modifier = Modifier
) {
    val media = screenState.categoriesMedia.collectAsLazyPagingItems()
    val genresNames = remember(screenState) { screenState.genres.drop(1).map { it.name } }

    when (media.loadState.refresh) {
        is LoadState.Loading -> {
            return
        }

        is LoadState.Error -> {
            HomeFailContent(
                errorStatus = screenState.errorStatus,
                listener = listener,
                modifier = Modifier.fillMaxSize()
            )
            return
        }

        else -> { }
    }

    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        state = lazyGridState,
        columns = GridCells.Adaptive(minSize = 101.33.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(top = 132.dp, bottom = 16.dp)
    ) {

        item(span = { GridItemSpan(maxLineSpan) }) {
            CategoriesChips(
                modifier = Modifier.fillMaxWidth(),
                categories = listOf(stringResource(R.string.genre_all)) + genresNames,
                selectedChipIndex = screenState.selectedGenreIndex,
                onChipSelected = { index -> listener.onGenreSelected(index) }
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            CategoriesChips(
                modifier = Modifier.fillMaxWidth(),
                categories = SortingType.entries.map { stringResource(it.titleId) },
                selectedChipIndex = screenState.selectedSortingType.ordinal,
                onChipSelected = { index ->
                    listener.onSortingSelected(SortingType.entries[index])
                }
            )
        }

        items(media.itemCount) { index ->
            media[index]?.let { mediaItem ->
                MovieCard(
                    modifier = Modifier.clickable {
                        listener.onMediaClick(mediaItem.id, mediaItem.isMovie)
                    },
                    title = mediaItem.title,
                    vote = mediaItem.rating,
                    imgUrl = mediaItem.posterPath,
                    width = null,
                    aspectRatio = 0.743f
                )
            }
        }
    }
}
