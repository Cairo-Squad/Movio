package com.cairosquad.ui.details.episodes.content

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailsScreenState

@Composable
fun BackgroundImageSection(uiState: EpisodesDetailsScreenState) {
    Box {
        when (uiState.basicDetailsSectionState) {
            EpisodesDetailsScreenState.ScreenStatus.LOADING -> {}
            EpisodesDetailsScreenState.ScreenStatus.SUCCESS -> {
                SeasonBackgroundImage(uiState)
            }

            EpisodesDetailsScreenState.ScreenStatus.ERROR -> {}
        }
    }
}