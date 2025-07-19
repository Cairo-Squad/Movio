package com.cairosquad.viewmodel.details.episodes

import com.cairosquad.viewmodel.exception.ErrorStatus

sealed class EpisodesDetailEffect {
    data object NavigateBack : EpisodesDetailEffect()
    data object PlayEpisode : EpisodesDetailEffect()
    data class ShowToast(val message: ErrorStatus) : EpisodesDetailEffect()
}