package com.cairosquad.viewmodel.rated

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class MyRatingsScreenState(
    val isLoading: Boolean = false,
    val ratedItems: Flow<PagingData<RatedItemUiState>> = emptyFlow()
)
