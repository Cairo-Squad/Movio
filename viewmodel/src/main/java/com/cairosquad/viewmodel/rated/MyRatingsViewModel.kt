package com.cairosquad.viewmodel.rated

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.cairosquad.domain.usecase.GetRatedItemsUseCase
import com.cairosquad.viewmodel.base.BaseViewModel
import com.cairosquad.viewmodel.rated.paging.RatedItemsPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyRatingsViewModel @Inject constructor(
    private val getRatedItemsUseCase: GetRatedItemsUseCase
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

    override fun onItemClicked(itemId: Long, isMovie: Boolean) {
        if (isMovie) {
            sendEffect(MyRatingsEffect.NavigateToMovieDetails(itemId))
        } else {
            sendEffect(MyRatingsEffect.NavigateToSeriesDetails(itemId))
        }
    }
}