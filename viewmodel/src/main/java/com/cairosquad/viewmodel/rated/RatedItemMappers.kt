package com.cairosquad.viewmodel.rated

import androidx.paging.PagingData
import androidx.paging.filter
import com.cairosquad.entity.Movie
import com.cairosquad.entity.Series
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Maps a Movie entity to a RatedItemUiState
 */
fun Movie.toUiState(userRating: Double): MyRatingsScreenState.RatedItemUiState = MyRatingsScreenState.RatedItemUiState(
    id = id,
    title = title,
    posterPath = posterPath,
    releaseDate = releaseDate.toString(),
    rating = rating.toDouble(),
    userRating = (userRating /2),
    isMovie = true
)

/**
 * Maps a Series entity to a RatedItemUiState
 */
fun Series.toUiState(userRating: Double): MyRatingsScreenState.RatedItemUiState = MyRatingsScreenState.RatedItemUiState(
    id = id,
    title = title,
    posterPath = posterPath,
    releaseDate = releaseDate.toString(),
    rating = rating.toDouble(),
    userRating = (userRating /2),
    isMovie = false
)

fun Flow<PagingData<MyRatingsScreenState.RatedItemUiState>>.removeItem(
    id: Long,
    isMovie: Boolean
): Flow<PagingData<MyRatingsScreenState.RatedItemUiState>> {
    return this.map { pagingData ->
        pagingData.filter { it.id != id || it.isMovie != isMovie }
    }
}