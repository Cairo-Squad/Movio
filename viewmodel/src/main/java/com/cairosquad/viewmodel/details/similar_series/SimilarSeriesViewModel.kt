package com.cairosquad.viewmodel.details.similar_series

import androidx.lifecycle.viewModelScope
import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.details.similar_series.SimilarSeriesScreenState.ScreenStatus
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SimilarSeriesViewModel @Inject constructor(
    private val manageSeriesUseCase: ManageSeriesUseCase
) : BaseViewModel<SimilarSeriesScreenState, SimilarSeriesEffect>(SimilarSeriesScreenState()),
    SimilarSeriesInteractionListener {

    fun fetchSimilarSeries(seriesId: Long) {
        tryToCall(
            onStart = {
                updateState {
                    it.copy(
                        screenStatus = ScreenStatus.LOADING
                    )
                }
            },
            block = {
                manageSeriesUseCase.getSimilarSeries(seriesId, 1)
            }, onSuccess = { seriesList ->
                updateState {
                    it.copy(
                        screenStatus = ScreenStatus.SUCCESS,
                        series = seriesList.map { it.toUiState() }
                    )
                }
            },
            onError = { e ->
                updateState {
                    it.copy(
                        screenStatus = ScreenStatus.ERROR,
                        errorStatus = when (e) {
                            is MovioException -> exceptionToErrorStatus(e)
                            else -> ErrorStatus.UNKNOWN_ERROR
                        }
                    )
                }
            },
            dispatcher = Dispatchers.IO
        )
    }

    override fun onClickBack() {
        sendEffect(SimilarSeriesEffect.NavigateBack)
    }

    override fun onSeriesClicked(seriesId: Long) {
        sendEffect(SimilarSeriesEffect.NavigateToSeriesDetails(seriesId))
    }

    override fun onRefresh(seriesId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            updateState { it.copy(isRefreshing = true) }
            fetchSimilarSeries(seriesId)
            delay(500L)
            updateState { it.copy(isRefreshing = false) }
        }
    }
}