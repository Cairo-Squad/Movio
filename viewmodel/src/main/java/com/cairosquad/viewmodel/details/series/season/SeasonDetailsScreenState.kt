package com.cairosquad.viewmodel.details.series.season

import com.cairosquad.entity.Episode
import com.cairosquad.entity.Season
import com.cairosquad.viewmodel.details.series.SeriesDetailsScreenState
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.util.TimeUtil

data class SeasonDetailsScreenState(
    val seasonSectionState: ScreenStatus = ScreenStatus.INITIAL,
    val episodesSectionState: ScreenStatus = ScreenStatus.INITIAL,
    val errorStatus: ErrorStatus? = null,
    val season: List<SeasonUiState> = emptyList(),
    val episodes: List<EpisodeUiState> = emptyList()
) {
    data class SeasonUiState(
        val number: Int = 0,
        val name: String = "",
        val episodesCount: Int = 0,
        val rating: Float = 0F,
        val posterPath: String = "",
        val overview: String = "",
        val airDate: String = ""
    )

    data class EpisodeUiState(
        val id: Long = 0L,
        val episodeNumber: Int = 0,
        val photoPath: String = "",
        val episodeName: String = "",
        val runtimeMinutes: Int = 0,
        val rating: Float = 0F
    )

    enum class ScreenStatus {
        INITIAL,
        LOADING,
        SUCCESS,
        ERROR
    }
}
fun Season.toUiState() = SeasonDetailsScreenState.SeasonUiState(
    number = seasonNumber,
    name = seasonName,
    episodesCount = episodesCount,
    rating = rating / 2,
    posterPath = posterPath,
    overview = overview,
    airDate = TimeUtil.convertLongToYear(airDate)
)
fun Episode.toUiState() = SeasonDetailsScreenState.EpisodeUiState(
    id = id,
    episodeNumber = episodeNumber,
    photoPath = photoPath,
    episodeName = episodeName,
    runtimeMinutes = runtimeMinutes,
    rating = rating / 2
)