package com.cairosquad.viewmodel.home

import com.cairosquad.entity.Genre
import com.cairosquad.viewmodel.exception.ErrorStatus

class HomeScreenState (
    val topRatingMovies: List<MovieUiState> = emptyList(),
    val trendingMovies: List<MovieUiState> = emptyList(),
    val freeToWatchMovies: List<MovieUiState> = emptyList(),
    val upcomingMovies: List<MovieUiState> = emptyList(),
    val nowPlayingMovies: List<MovieUiState> = emptyList(),
    val moreRecommendedMovies: List<MovieUiState> = emptyList(),
    val topRatingSeries: List<SeriesUiState> = emptyList(),
    val airingTodaySeries: List<SeriesUiState> = emptyList(),
    val onTvSeries: List<SeriesUiState> = emptyList(),
    val moreRecommendedSeries: List<SeriesUiState> = emptyList(),
    val screenStatus: HomeScreenState.ScreenStatus = HomeScreenState.ScreenStatus.LOADING,
    val errorStatus: ErrorStatus? = null,
    val selectedTab: TabType = TabType.ALL,
) {
    data class MovieUiState(
        val id: Long = 0L,
        val title: String = "",
        val rating: Float = 0f,
        val posterPath: String = "",
        val genres: List<Genre> =emptyList()
    )

    data class SeriesUiState(
        val id: Long = 0L,
        val title: String = "",
        val rating: Float = 0f,
        val posterPath: String = "",
        val genres: List<Genre> = emptyList()
    )

    enum class ScreenStatus {
        LOADING,
        SUCCESS,
        FAILED
    }
    enum class TabType {
        ALL,
        MOVIES,
        TV_SHOWS,
        CATEGORIES
    }
}