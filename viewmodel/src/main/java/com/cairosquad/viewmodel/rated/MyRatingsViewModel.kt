package com.cairosquad.viewmodel.rated

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.cairosquad.domain.usecase.GetRatedItemsUseCase
import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.viewmodel.R
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.rated.mappers.removeItem
import com.cairosquad.viewmodel.rated.paging.RatedItemsPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRatingsViewModel @Inject constructor(
    private val getRatedItemsUseCase: GetRatedItemsUseCase,
    private val manageSeriesUseCase: ManageSeriesUseCase,
    private val manageMoviesUseCase: ManageMoviesUseCase
) : BaseViewModel<MyRatingsScreenState, MyRatingsEffect>(initialState = MyRatingsScreenState()),
    MyRatingsInteractionListener {

    init {
        loadRatedItems()
    }

    private fun loadRatedItems() {
        updateState {
            it.copy(
                isLoading = true,
                ratedItems = Pager(
                    config = PagingConfig(
                        pageSize = 20,
                        enablePlaceholders = false
                    ),
                    pagingSourceFactory = {
                        RatedItemsPagingSource(getRatedItemsUseCase)
                    }
                ).flow.cachedIn(viewModelScope)
            )
        }
        updateState { it.copy(isLoading = false) }
    }

    override fun onBackPressed() {
        sendEffect(MyRatingsEffect.NavigateBack)
    }

    override fun onUndoClicked() {
        val item = screenState.value.deletedItems.last().split(", ")
        when (item[0]) {
            "movie" -> {
                onUndoClickMovie(item)
            }

            "tv" -> {
                onUndoClickSeries(item)
            }
        }
    }

    private fun onUndoClickSeries(item: List<String>) {
        tryToCall(
            block = {
                manageSeriesUseCase.addSeriesRating(item[1].toLong(), item[2].toFloat() * 2)
            },
            onSuccess = {
                loadRatedItems()
                showSnackBar(R.string.series_rate_restore_success, true)
            },
            onError = {
                showSnackBar(R.string.series_rate_restore_fail, false)
            }
        )
    }

    private fun onUndoClickMovie(item: List<String>) {
        tryToCall(
            block = {
                manageMoviesUseCase.addMovieRating(item[1].toLong(), item[2].toFloat() * 2)
            },
            onSuccess = {
                loadRatedItems()
                showSnackBar(R.string.movie_rate_restore_success, true)
            },
            onError = {
                showSnackBar(R.string.movie_rate_restore_fail, false)
            }
        )
    }


    override fun onMovieClicked(movieId: Long) {
        sendEffect(MyRatingsEffect.NavigateToMovieDetails(movieId))
    }

    override fun onSeriesClicked(seriesId: Long) {
        sendEffect(MyRatingsEffect.NavigateToSeriesDetails(seriesId))
    }

    override fun onMovieDelete(movieId: Long, rating: Int) {
        tryToCall(
            onStart = { onMovieDeleteStart(movieId, rating) },
            block = { manageMoviesUseCase.deleteMovieRating(movieId) },
            onSuccess = { onMovieDeleteSuccess() },
            onError = ::onMovieDeleteError
        )
    }

    private fun onMovieDeleteStart(movieId: Long, rating: Int) {
        updateState { state ->
            state.copy(
                ratedItems = state.ratedItems.removeItem(movieId, isMovie = true),
                deletedItems = state.deletedItems + "movie, $movieId, $rating",
                deletedMovies = state.deletedMovies + movieId
            )
        }
    }

    private fun onMovieDeleteSuccess() {
        showSnackBar(R.string.movie_rate_remove_success, true)
    }

    private fun onMovieDeleteError(throwable: Throwable) {
        showSnackBar(R.string.movie_rate_remove_fail, false)
    }

    override fun onSeriesDelete(seriesId: Long, rating: Int) {
        tryToCall(
            onStart = { onSeriesDeleteStart(seriesId, rating) },
            block = { manageSeriesUseCase.deleteSeriesRating(seriesId = seriesId) },
            onSuccess = { onSeriesDeleteSuccess() },
            onError = ::onSeriesDeleteError

        )
    }

    private fun onSeriesDeleteStart(seriesId: Long, rating: Int) {
        updateState { state ->
            state.copy(
                ratedItems = state.ratedItems.removeItem(seriesId, isMovie = false),
                deletedItems = state.deletedItems + "tv, $seriesId, $rating",
                deletedMovies = state.deletedSeries + seriesId
            )
        }
    }

    private fun onSeriesDeleteSuccess() {
        showSnackBar(R.string.series_rate_remove_success, true)
    }

    private fun onSeriesDeleteError(throwable: Throwable) {
        showSnackBar(R.string.series_rate_remove_fail, false)
    }

    private fun showSnackBar(messageId: Int, isProcessSuccess: Boolean) {
        viewModelScope.launch {
            updateState {
                it.copy(
                    showSnackBar = true,
                    isProcessSuccess = isProcessSuccess,
                    snackMessageId = messageId
                )
            }
            delay(2000)
            updateState { it.copy(showSnackBar = false) }
        }
    }
}