package com.cairosquad.ui.details.episodes.content

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import com.cairosquad.viewmodel.details.episodes.EpisodesDetailsScreenState

@Composable
fun BackgroundImageSection(state: EpisodesDetailsScreenState) {
    Box {
        when (state.basicDetailsSectionState) {
            EpisodesDetailsScreenState.ScreenStatus.LOADING -> {}
            EpisodesDetailsScreenState.ScreenStatus.SUCCESS -> {
                SeasonBackgroundImage(state)
            }

            EpisodesDetailsScreenState.ScreenStatus.ERROR -> {}
        }
    }
}