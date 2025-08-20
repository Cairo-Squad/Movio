package com.cairosquad.viewmodel.details.episodes

import com.cairosquad.entity.Episode
import com.cairosquad.entity.Season
import com.cairosquad.viewmodel.util.localizeNumbers
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace

fun Season.toUiState() = EpisodesDetailsScreenState.SeasonUiState(
    seasonNumber = seasonNumber,
    posterUrl = posterPath,
    episodesCount = episodesCount
)

fun Episode.toUiState() = EpisodesDetailsScreenState.EpisodeUiState(
    id = id,
    name = episodeName.localizeNumbers(),
    number = episodeNumber,
    runtime = runtimeInMinutes.takeIf { it != 0 },
    rating = rating.roundToFirstDecimalPlace(),
    imageUrl = photoPath
)


