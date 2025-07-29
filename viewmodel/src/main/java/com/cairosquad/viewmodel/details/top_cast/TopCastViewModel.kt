package com.cairosquad.viewmodel.details.top_cast

import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.details.top_cast.TopCastScreenState.ScreenStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class TopCastViewModel @Inject constructor(
    private val manageMoviesUseCase: ManageMoviesUseCase,
    private val manageSeriesUseCase: ManageSeriesUseCase
) : BaseViewModel<TopCastScreenState, Nothing>(TopCastScreenState()) {

    private val mediaId: Long = 0 // TODO: get
    private val isMovie: Boolean = false // TODO: get
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO // TODO: get


    init {
        getTopCast()
    }

    fun getTopCast() {
        tryToCall(
            onStart = {
                updateState { it.copy(screenStatus = ScreenStatus.LOADING) }
            },
            block = {
                getTopCastByMediaType()
            },
            onSuccess = ::handleSuccess,
            onError = { throwable ->
                updateState {
                    it.copy(
                        screenStatus = ScreenStatus.ERROR,
                        error = throwable.message
                    )
                }
            },
            dispatcher = dispatcher
        )
    }

    private suspend fun getTopCastByMediaType(): List<Artist> {
        return if (isMovie) {
            manageMoviesUseCase.getMovieTopCast(mediaId)
        } else {
            manageSeriesUseCase.getSeriesTopCast(mediaId, 1)
        }
    }

    private fun handleSuccess(cast: List<Artist>) {
        updateState {
            it.copy(
                screenStatus = ScreenStatus.SUCCESS,
                cast = cast.map { it.toTopCastUiState() })
        }
    }
}