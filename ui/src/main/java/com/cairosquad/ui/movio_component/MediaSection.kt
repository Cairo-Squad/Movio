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

@Composable
fun MediaSection(
    mediaList: List<MediaSectionItem>,
    onClickMedia: (Long) -> Unit,
    sectionTitle: String,
    mediaSectionLayoutType: MediaSectionLayoutType,
    modifier: Modifier = Modifier,
    seeAllAction: (() -> Unit)? = null,
) {
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
        val fakeItems = listOf(
            MediaSectionItem(
                id = 1311031,
                title = "Demon Slayer: Kimetsu no Yaiba Infinity Castle",
                photoPath = "https://image.tmdb.org/t/p/w500/aFRDH3P7TX61FVGpaLhKr6QiOC1.jpg",
                rating = 4.5f
            ),
            MediaSectionItem(
                id = 1061474,
                title = "Superman",
                photoPath = "https://image.tmdb.org/t/p/w500/ombsmhYUqR4qqOLOxAyr5V8hbyv.jpg",
                rating = 4.5f
            ),
            MediaSectionItem(
                id = 541671,
                title = "Ballerina",
                photoPath = "https://image.tmdb.org/t/p/w500/2VUmvqsHb6cEtdfscEA6fqqVzLg.jpg",
                rating = 4.5f
            ),
            MediaSectionItem(
                id = 1087192,
                title = "How to Train Your Dragon",
                photoPath = "https://image.tmdb.org/t/p/w500/q5pXRYTycaeW6dEgsCrd4mYPmxM.jpg",
                rating = 4.5f
            ),
            MediaSectionItem(
                id = 1269208,
                title = "Wall to Wall",
                photoPath = "https://image.tmdb.org/t/p/w500/5hlNv3Kd9xovvSgrslWhMriGpZ8.jpg",
                rating = 4.5f
            ),
            MediaSectionItem(
                id = 617126,
                title = "The Fantastic Four: First Steps",
                photoPath = "https://image.tmdb.org/t/p/w500/x26MtUlwtWD26d0G0FXcppxCJio.jpg",
                rating = 4.5f
            ),
            MediaSectionItem(
                id = 1071585,
                title = "M3GAN 2.0",
                photoPath = "https://image.tmdb.org/t/p/w500/4a63rQqIDTrYNdcnTXdPsQyxVLo.jpg",
                rating = 4.5f
            )
        )
    }
}

sealed class MediaSectionLayoutType {
    data class LazyHorizontalGrid(val rowsCount: Int) : MediaSectionLayoutType()
    data class LazyVerticalGrid(val minWidthDp: Int) : MediaSectionLayoutType()
    object LazyRow: MediaSectionLayoutType()
    object LazyColumn: MediaSectionLayoutType()
}