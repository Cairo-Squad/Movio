package com.cairosquad.viewmodel.home

import com.cairosquad.viewmodel.R
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.util.MediaContentType

data class HomeScreenState(

    val popularMovies: List<MediaUiState> = emptyList(),
    val popularSeries: List<MediaUiState> = emptyList(),

    val categoriesMedia: List<MediaUiState> = emptyList(),

    val sections: Map<MediaContentType, SectionUiState> = mapOf(),

    val screenStatus: ScreenStatus = ScreenStatus.LOADING,
    val errorStatus: ErrorStatus? = null,

    val selectedGenreIndex: Int = 0,
    val genres: List<GenreUiState> = listOf(GenreUiState.defaultGenre),

    val selectedSortingType: SortingType = SortingType.ALL,
    val selectedTab: Tab = Tab.MOVIES,
    val isRefreshing: Boolean = false
) {

    data class SectionUiState(
        val movies: List<MediaUiState> = emptyList(),
        val series: List<MediaUiState> = emptyList(),
        val isLoading: Boolean = false
    )

    data class MediaUiState(
        val id: Long,
        val title: String,
        val rating: Float,
        val posterPath: String,
        val genres: List<GenreUiState>,
        val isMovie: Boolean
    )

    data class GenreUiState(
        val id: Long?,
        val name: String,
    ) {
        companion object {
            val defaultGenre = GenreUiState(
                id = null,
                name = "ALL GENRES"
            )
        }
    }

    enum class ScreenStatus {
        LOADING,
        SUCCESS,
        FAILED
    }

    enum class Tab {
        MOVIES,
        TV_SHOWS,
        CATEGORIES
    }

    enum class SortingType(val titleId: Int) {
        ALL(R.string.sorting_type_all),
        POPULARITY(R.string.popularity),
        LATEST(R.string.latest)
    }


}