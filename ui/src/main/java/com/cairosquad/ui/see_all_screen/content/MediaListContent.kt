package com.cairosquad.ui.see_all_screen.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.cairosquad.ui.movio_component.TrendingMovieCard
import com.cairosquad.viewmodel.see_all.SeeAllInteractionsListener
import com.cairosquad.viewmodel.see_all.SeeAllScreenState
import java.util.Locale

@Composable
fun MediaListContent(
    media: LazyPagingItems<SeeAllScreenState.MediaUiState>,
    listener: SeeAllInteractionsListener,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(media.itemCount) { index ->
            media[index]?.let { mediaItem ->
                TrendingMovieCard(
                    modifier = Modifier.clickable {
                        listener.onMediaClick(mediaItem.id, mediaItem.isMovie)
                    },
                    imgUrl = mediaItem.posterPath,
                    rating = String.format(
                        Locale.getDefault(), "%.1f", mediaItem.rating
                    ),
                    movieTitle = mediaItem.title,
                    movieCategory = mediaItem.genres.firstOrNull()?.name.orEmpty()
                )
            }
        }
    }
}