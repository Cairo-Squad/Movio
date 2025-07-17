package com.cairosquad.viewmodel.details.episodes

import com.cairosquad.viewmodel.exception.ErrorStatus

sealed class EpisodesDetailEffect {
    data object NavigateBack : EpisodesDetailEffect()
    data object PlayEpisodes : EpisodesDetailEffect()
    data class ErrorHappened(val message: ErrorStatus) : EpisodesDetailEffect()
}