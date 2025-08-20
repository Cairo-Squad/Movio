package com.cairosquad.viewmodel.details.top_cast

import com.cairosquad.domain.usecase.ManageMoviesUseCase
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Artist
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.details.top_cast.TopCastScreenState.ScreenStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher

@HiltViewModel(assistedFactory = TopCastViewModel.Factory::class)
class TopCastViewModel @AssistedInject constructor(
    private val manageMoviesUseCase: ManageMoviesUseCase,
    private val manageSeriesUseCase: ManageSeriesUseCase,
    @Assisted private val mediaId: Long,
    @Assisted private val isMovie: Boolean,
    @Assisted private val dispatcher: CoroutineDispatcher,
) : BaseViewModel<TopCastScreenState, Nothing>(TopCastScreenState()) {

    @AssistedFactory
    interface Factory {
        fun create(
            mediaId: Long,
            isMovie: Boolean,
            dispatcher: CoroutineDispatcher
        ): TopCastViewModel
    }

    init {
        getTopCast()
    }

    fun getTopCast() {
        tryToCall(
            onStart = ::onFetchStart,
            block = { getTopCastByMediaType() },
            onSuccess = ::onFetchSuccess,
            onError = ::onFetchError,
            dispatcher = dispatcher
        )
    }

    private fun onFetchStart() {
        updateState { it.copy(screenStatus = ScreenStatus.LOADING) }
    }

    private suspend fun getTopCastByMediaType(): List<Artist> {
        return if (isMovie) {
            manageMoviesUseCase.getMovieTopCast(mediaId)
        } else {
            manageSeriesUseCase.getSeriesTopCast(mediaId, FIRST_PAGE)
        }
    }

    private fun onFetchSuccess(cast: List<Artist>) {
        updateState {
            it.copy(
                screenStatus = ScreenStatus.SUCCESS,
                cast = cast.map { it.toUiState() }
            )
        }
    }

    private fun onFetchError(throwable: Throwable) {
        updateState {
            it.copy(
                screenStatus = ScreenStatus.ERROR,
                error = throwable.message
            )
        }
    }

     companion object {
         private const val FIRST_PAGE = 1
    }
}