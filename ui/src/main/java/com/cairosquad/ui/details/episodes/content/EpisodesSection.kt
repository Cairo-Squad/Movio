package com.cairosquad.ui.details.episodes.content

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cairosquad.ui.BuildConfig
import com.cairosquad.ui.details.episodes.composable.EpisodeCard
import com.cairosquad.ui.movio_component.LoadingMovieImage
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailsScreenState

fun LazyListScope.EpisodesSection(uiState: EpisodesDetailsScreenState) {
    when (uiState.episodesSectionState) {
        EpisodesDetailsScreenState.ScreenStatus.LOADING -> {
            items(10) {
                LoadingMovieImage(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .height(74.dp)
                        .fillMaxSize(),
                )
            }
        }

        EpisodesDetailsScreenState.ScreenStatus.SUCCESS -> {
            items(uiState.episodes) { episode ->
                EpisodeCard(
                    episodeNumber = episode.number.toString().padStart(2, '0'),
                    episodeImageUrl = BuildConfig.IMAGE_BASE_URL + episode.imageUrl,
                    episodeName = episode.name,
                    episodeDuration = episode.runtime,
                    episodeRating = String.format("%.1f", episode.rating),
                )
            }
        }

        EpisodesDetailsScreenState.ScreenStatus.ERROR -> {}
    }
}