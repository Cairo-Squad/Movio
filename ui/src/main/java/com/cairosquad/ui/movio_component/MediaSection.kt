package com.cairosquad.ui.movio_component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.viewmodel.home.HomeScreenState

@Composable
fun MediaSection(
    mediaList: List<MediaSectionItem>,
    onClickMedia: (Long) -> Unit,
    sectionTitle: String,
    mediaSectionLayoutType: MediaSectionLayoutType,
    modifier: Modifier = Modifier,
    seeAllAction: (() -> Unit)? = null,
) {

    if (mediaList.isEmpty()) return // TODO: add empty state

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionHeader(
            title = sectionTitle,
            modifier = Modifier,
            actionText = stringResource(com.cairosquad.ui.R.string.see_all).takeIf { seeAllAction != null },
            actionIcon = ImageVector.vectorResource(R.drawable.arrow),
            onActionClick = { seeAllAction?.invoke() }
        )

        when (mediaSectionLayoutType) {
            is MediaSectionLayoutType.LazyHorizontalGrid -> {
                LazyHorizontalGrid(
                    modifier = Modifier.height((mediaSectionLayoutType.rowsCount * 112 - 12).dp),
                    rows = GridCells.Fixed(count = mediaSectionLayoutType.rowsCount),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                ) {
                    items(mediaList) { media ->
                        TrendingMovieCard(
                            modifier = Modifier
                                .width(240.dp)
                                .clickable { onClickMedia(media.id) },
                            imgUrl = media.photoPath,
                            movieTitle = media.title,
                            movieCategory = "Documentary",
                            rating = media.rating.toString()
                        )
                    }
                }
            }
            is MediaSectionLayoutType.LazyVerticalGrid -> {
                LazyVerticalGrid (
                    modifier = Modifier.heightIn(max = 10000.dp),
                    columns = GridCells.Adaptive(minSize = mediaSectionLayoutType.minWidthDp.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    userScrollEnabled = false
                ) {
                    items(mediaList) { media ->
                        MovieCard(
                            modifier = Modifier
                                .clickable { onClickMedia(media.id) },
                            imgUrl = media.photoPath,
                            title = media.title,
                            vote = media.rating,
                            aspectRatio = 0.877f,
                            width = null
                        )
                    }
                }
            }
            MediaSectionLayoutType.LazyColumn -> {
                LazyColumn (
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    userScrollEnabled = false
                ) {
                    items (mediaList) { media ->
                        MovieCard(
                            modifier = Modifier
                                .width(158.dp)
                                .clickable { onClickMedia(media.id) },
                            imgUrl = media.photoPath,
                            title = media.title,
                            vote = media.rating,
                            aspectRatio = 0.877f,
                            width = 158.dp
                        )
                    }
                }
            }
            MediaSectionLayoutType.LazyRow -> {
                LazyRow (
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)

                ) {
                    items (mediaList) { media ->
                        MovieCard(
                            modifier = Modifier
                                .width(124.dp)
                                .clickable { onClickMedia(media.id) },
                            imgUrl = media.photoPath,
                            title = media.title,
                            vote = media.rating,
                            aspectRatio = 0.775f,
                            width = 124.dp
                        )
                    }
                }
            }
        }
    }
}

data class MediaSectionItem(
    val id: Long,
    val title: String,
    val photoPath: String,
    val rating: Float,
){
    companion object {

        fun fromHomeMovieUiState(movie: HomeScreenState.MovieUiState): MediaSectionItem {
            return MediaSectionItem(
                id = movie.id,
                title = movie.title,
                photoPath = movie.posterPath,
                rating = movie.rating,
            )
        }
    }
}

sealed class MediaSectionLayoutType {
    data class LazyHorizontalGrid(val rowsCount: Int) : MediaSectionLayoutType()
    data class LazyVerticalGrid(val minWidthDp: Int) : MediaSectionLayoutType()
    object LazyRow: MediaSectionLayoutType()
    object LazyColumn: MediaSectionLayoutType()
}