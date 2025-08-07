package com.cairosquad.viewmodel.library.view_all_favorite

import androidx.paging.PagingData
import com.cairosquad.viewmodel.exception.ErrorStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class ViewAllFavoriteScreenState(
    val screenStatus: SectionStatus = SectionStatus.LOADING,
    val errorStatus: ErrorStatus? = null,

    val isRefreshing: Boolean = false,

    val movies: Flow<PagingData<MovieUiState>> = flowOf(PagingData.empty()),
    val deletedMoviesIds: List<Long> = emptyList(),
    val series: Flow<PagingData<SeriesUiState>> = flowOf(PagingData.empty()),
    val deletedSeriesIds: List<Long> = emptyList(),

    ) {

    data class SeriesUiState(
        val id: Long = 0L,
        val title: String = "",
        val rating: Float = 0F,
        val posterPath: String = "",
        val genres: List<String> = emptyList(),
        val seasonsCount: Int = 0,
        val releaseDate: String = "",
        val overview: String = "",
        val trailerPath: String = ""
    )

    data class MovieUiState(
        val id: Long = 0,
        val title: String = "",
        val rating: Float = 0f,
        val posterPath: String = "",
        val genres: List<String> = emptyList(),
        val overview: String = "",
        val releaseDate: String = "0",
        val runtimeMinutes: String = "",
        val trailerPath: String = ""
    )

    enum class SectionStatus {
        LOADING,
        SUCCESS,
        ERROR
    }
}