package com.cairosquad.viewmodel.home.state

import com.cairosquad.entity.Genre
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.exception.ErrorStatus

data class DiscoverScreenState(
    val trendingMovies: List<MovieUiState> = emptyList(),
    val trendingSeries: List<SeriesUiState> = emptyList(),

    val topRatedMovies: List<MovieUiState> = emptyList(),
    val topRatedSeries: List<SeriesUiState> = emptyList(),

    val moreRecommendedMovies: List<MovieUiState> = emptyList(),
    val moreRecommendedSeries: List<SeriesUiState> = emptyList(),

    val freeToWatchMovies: List<MovieUiState> = emptyList(),
    val freeToWatchSeries: List<SeriesUiState> = emptyList(),

    val upcomingMovies: List<MovieUiState> = emptyList(),
    val upcomingSeries: List<SeriesUiState> = emptyList(),

    val screenStatus: ScreenStatus = ScreenStatus.LOADING,
    val errorStatus: ErrorStatus? = null,
    val selectedFilter: FilterType = FilterType.ALL,
    val selectedTab: TabType = TabType.ALL,
    val genres: List<GenreUiState> = listOf(GenreUiState.defaultGenre),
    val selectedGenreIndex: Int = 0,
    val filters: List<String> = listOf("All","Popularity","Latest")
){
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
    ) {
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
fun Movie.toDiscoverMovieUiState() = DiscoverScreenState.MovieUiState(
    id = id,
    title = title,
    rating = rating,
    posterPath = posterPath,
    genres = genres.map { DiscoverScreenState.GenreUiState(it.id, it.name) }
)

fun Series.toDiscoverSeriesUiState() = DiscoverScreenState.SeriesUiState(
    id = id,
    title = title,
    rating = rating,
    posterPath = posterPath,
    genres = genres.map { DiscoverScreenState.GenreUiState(it.id, it.name) }
)
fun Genre.toDiscoverGenreUiState() = DiscoverScreenState.GenreUiState(
    id = id,
    name = name
)