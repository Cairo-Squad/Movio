package com.cairosquad.ui.see_all_screen.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.viewmodel.see_all.SeeAllInteractionsListener
import com.cairosquad.viewmodel.see_all.SeeAllScreenState

@Composable
fun MediaGridContent(
    media: LazyPagingItems<SeeAllScreenState.MediaUiState>,
    listener: SeeAllInteractionsListener,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 101.33.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp, top = 8.dp)
    ) {
        items(media.itemCount) { index ->
            media[index]?.let { item ->
                MovieCard(
                    modifier = Modifier.clickable {
                        listener.onMediaClick(item.id, item.isMovie)
                    },
                    title = item.title,
                    vote = item.rating,
                    imgUrl = item.posterPath,
                    width = null,
                    aspectRatio = 0.743f
                )
            }
        }
    }
}