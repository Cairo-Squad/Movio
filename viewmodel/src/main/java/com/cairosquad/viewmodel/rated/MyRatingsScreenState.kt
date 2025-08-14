package com.cairosquad.viewmodel.rated

import androidx.paging.PagingData
import com.cairosquad.viewmodel.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class MyRatingsScreenState(
    val isLoading: Boolean = false,
    val ratedItems: Flow<PagingData<RatedItemUiState>> = emptyFlow(),
    val deletedSeries: Set<Long> = emptySet(),
    val deletedMovies: Set<Long> = emptySet(),
    val deletedItems: List<String> = emptyList(),
    val showSnackBar: Boolean = false,
    val isProcessSuccess: Boolean = false,
    val snackMessageId: Int = R.string.movie_favorite_remove_success
) {
    data class RatedItemUiState(
        val id: Long,
        val title: String,
        val posterPath: String?,
        val releaseDate: String?,
        val rating: Double,
        val userRating: Int = 0,
        val isMovie: Boolean
    )
}
