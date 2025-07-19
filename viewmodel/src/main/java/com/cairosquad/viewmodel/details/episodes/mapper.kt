package com.cairosquad.viewmodel.details.episodes

import com.cairosquad.entity.Episode
import com.cairosquad.entity.Season

fun Season.toUiState() = EpisodesDetailsScreenState.SeasonUiState(
    seasonNumber = seasonNumber,
    posterUrl = posterPath,
    episodesCount = episodesCount
)

fun Episode.toUiState() = EpisodesDetailsScreenState.EpisodeUiState(
    id = id,
    name = episodeName,
    number = episodeNumber,
    runtime = runtimeMinutes,
    rating = rating / 2,
    imageUrl = photoPath
)


