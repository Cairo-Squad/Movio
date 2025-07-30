package com.cairosquad.viewmodel.see_all

import androidx.paging.PagingData
import com.cairosquad.viewmodel.R
import com.cairosquad.viewmodel.exception.ErrorStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import com.cairosquad.viewmodel.home.HomeScreenState.GenreUiState

data class SeeAllScreenState(
    val mediaList: Flow<PagingData<MediaUiState>> = flowOf(PagingData.empty()),
    val screenStatus: ScreenStatus = ScreenStatus.LOADING,
    val errorStatus: ErrorStatus? = null,
    val isRefreshing: Boolean = false,
    val genres: List<GenreUiState> = listOf(GenreUiState.defaultGenre),
    val selectedGenreIndex: Int = 0,
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
        val id: Long? = 0L,
        val name: String = "",
    ) {
        companion object {
            val defaultGenre = GenreUiState(
                id = null,
                name ="",
            )
        }
    }

    enum class ScreenStatus {
        LOADING,
        SUCCESS,
        FAILED,
        Empty
    }
}