package com.cairosquad.viewmodel.details.series.season

import com.cairosquad.entity.Season
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.util.TimeUtil
import com.cairosquad.viewmodel.util.roundToFirstDecimalPlace

data class SeasonDetailsScreenState(
    val seriesTitle: String = "",
    val seasonSectionState: ScreenStatus = ScreenStatus.LOADING,
    val errorStatus: ErrorStatus? = null,
    val season: List<SeasonUiState> = emptyList(),
) {
    data class SeasonUiState(
        val seriesId: Long = 0L,
        val number: Int = 0,
        val name: String = "",
        val episodesCount: Int = 0,
        val rating: Float = 0F,
        val posterPath: String = "",
        val overview: String = "",
        val airDate: String = "",
        val timeOfPublish: String = ""
    )

    enum class ScreenStatus {
        LOADING,
        SUCCESS,
        ERROR
    }
}
fun Season.toUiState():SeasonDetailsScreenState.SeasonUiState {
    return SeasonDetailsScreenState.SeasonUiState(
        seriesId = seriesId,
        number = seasonNumber,
        name = seasonName,
        episodesCount = episodesCount,
        rating = rating.roundToFirstDecimalPlace(),
        posterPath = posterPath,
        overview = overview,
        airDate = TimeUtil.convertLongToYear(airDate),
        timeOfPublish = TimeUtil.convertLongToNamedDate(airDate)
    )
}