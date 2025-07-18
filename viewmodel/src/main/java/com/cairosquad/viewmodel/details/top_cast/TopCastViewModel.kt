package com.cairosquad.viewmodel.details.top_cast

import com.cairosquad.domain.usecase.movies.GetMovieDetailsUseCase
import com.cairosquad.domain.usecase.series.GetSeriesDetailsUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.viewmodel.base.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TopCastViewModel(
    private val mediaId: Long,
    private val isMovie: Boolean,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getSeriesDetailsUseCase: GetSeriesDetailsUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel<TopCastScreenState, Nothing>(TopCastScreenState()) {
    init {
        getTopCast()
    }

    fun getTopCast() {
        tryToCall(
            block = { getTopCastByMediaType() },
            onSuccess = ::handleSuccess,
            onError = { throwable -> updateState { it.copy(isLoading = false, error = throwable.message) } },
            dispatcher = dispatcher
        )

    }

    private suspend fun getTopCastByMediaType(): List<Artist> {
        return if (isMovie) {
            getMovieDetailsUseCase.getMovieTopCast(mediaId)
        } else {
            getSeriesDetailsUseCase.getSeriesTopCast(mediaId, 1)
        }
    }

    private fun handleSuccess(cast: List<Artist>) {
        updateState { it.copy(isLoading = false, cast = cast.map { it.toTopCastUiState() }) }
    }
}