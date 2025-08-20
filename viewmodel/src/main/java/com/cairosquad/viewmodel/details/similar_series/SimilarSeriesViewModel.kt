package com.cairosquad.viewmodel.details.similar_series

import com.cairosquad.domain.exception.MovioException
import com.cairosquad.domain.usecase.ManageSeriesUseCase
import com.cairosquad.entity.Series
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.details.similar_series.SimilarSeriesScreenState.ScreenStatus
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.exception.exceptionToErrorStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class SimilarSeriesViewModel @Inject constructor(
    private val manageSeriesUseCase: ManageSeriesUseCase
) : BaseViewModel<SimilarSeriesScreenState, SimilarSeriesEffect>(SimilarSeriesScreenState()),
    SimilarSeriesInteractionListener {

    fun fetchSimilarSeries(seriesId: Long) {
        tryToCall(
            onStart = ::onFetchStart,
            block = { manageSeriesUseCase.getSimilarSeries(seriesId, FIRST_PAGE ) },
            onSuccess = ::onFetchSuccess,
            onError = ::onFetchError,
            dispatcher = Dispatchers.IO
        )
    }

    private fun onFetchStart() {
        updateState { it.copy(screenStatus = ScreenStatus.LOADING) }
    }

    private fun onFetchSuccess(seriesList: List<Series>) {
        updateState {
            it.copy(
                screenStatus = ScreenStatus.SUCCESS,
                series = seriesList.map { it.toUiState() }
            )
        }
    }

    private fun onFetchError(e: Throwable) {
        updateState {
            it.copy(
                screenStatus = ScreenStatus.ERROR,
                errorStatus = when (e) {
                    is MovioException -> exceptionToErrorStatus(e)
                    else -> ErrorStatus.UNKNOWN_ERROR
                }
            )
        }
    }

    override fun onBackClick() {
        sendEffect(SimilarSeriesEffect.NavigateBack)
    }

    override fun onSeriesClick(seriesId: Long) {
        sendEffect(SimilarSeriesEffect.NavigateToSeriesDetails(seriesId))
    }

    override fun onRefresh(seriesId: Long) {
        updateState { it.copy(isRefreshing = true) }
        fetchSimilarSeries(seriesId)
        updateState { it.copy(isRefreshing = false) }
    }

     companion object {
         private const val FIRST_PAGE = 1
    }
}