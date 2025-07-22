package com.cairosquad.viewmodel.home

import com.cairosquad.viewmodel.exception.ErrorStatus

data class HomeScreenState (
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
    val randomMovies:List<MovieUiState> = emptyList(),
    val randomSeries: List<SeriesUiState> = emptyList(),
    val screenStatus: ScreenStatus = ScreenStatus.LOADING,
    val errorStatus: ErrorStatus? = null,
    val selectedFilter: FilterType = FilterType.ALL,
    val selectedTab: TabType = TabType.ALL,
    val genres: List<GenreUiState> = listOf(GenreUiState.defaultGenre),
    val selectedGenreIndex: Int = 0,


    ) {
    data class MovieUiState(
        val id: Long = 0L,
        val title: String = "",
        val rating: Float = 0f,
        val posterPath: String = "",
        val genres: List<GenreUiState> = emptyList()
    )

    data class SeriesUiState(
        val id: Long = 0L,
        val title: String = "",
        val rating: Float = 0f,
        val posterPath: String = "",
        val genres: List<GenreUiState> = emptyList()
    )

    data class GenreUiState(
        val id: Long? = 0L,
        val name: String = "",

    )
    {
        companion object{
            val defaultGenre= GenreUiState(
                id=null,
                name="All"
            )
        }
    }

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
    enum class FilterType {
        ALL,
        POPULARITY,
        LATEST
    }


}