package com.cairosquad.viewmodel.home

import androidx.paging.PagingData
import com.cairosquad.viewmodel.R
import com.cairosquad.viewmodel.exception.ErrorStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class HomeScreenState(
    val movieSections: MovieSectionsState = MovieSectionsState(),
    val seriesSections: SeriesSectionsState = SeriesSectionsState(),

    val popularMovies: List<MediaUiState> = emptyList(),
    val popularSeries: List<MediaUiState> = emptyList(),
    val profileImage: String = "",

    val categoriesMedia: Flow<PagingData<MediaUiState>> = flowOf(PagingData.empty()),

    val dataRequestStatus: DataRequestStatus = DataRequestStatus.LOADING,
    val errorStatus: ErrorStatus? = null,

    val selectedGenreIndex: Int = 0,
    val genres: List<GenreUiState> = listOf(GenreUiState.defaultGenre),

    val selectedSortingType: SortingType = SortingType.ALL,
    val selectedTab: Tab = Tab.MOVIES,
    val isRefreshing: Boolean = false
) {

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
                name = "ALL GENRE",
            )
        }
    }

    enum class DataRequestStatus {
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

    data class MovieSectionsState(
        val topRating: List<MediaUiState> = emptyList(),
        val nowPlaying: List<MediaUiState> = emptyList(),
        val upComing: List<MediaUiState> = emptyList(),
        val moreRecommended: List<MediaUiState> = emptyList(),
    )

    data class SeriesSectionsState(
        val topRating: List<MediaUiState> = emptyList(),
        val airingToday: List<MediaUiState> = emptyList(),
        val onTv: List<MediaUiState> = emptyList(),
        val moreRecommended: List<MediaUiState> = emptyList(),
    )

}