package com.cairosquad.ui.rated.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.LoadingMovieCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.viewmodel.rated.MyRatingsScreenState

@Composable
fun RatedItemsList(
    ratedItems: LazyPagingItems<MyRatingsScreenState.RatedItemUiState>,
    onItemClick: (Long, Boolean) -> Unit,
    onMovieDelete: (Long, Double) -> Unit,
    onSeriesDelete: (Long, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
            .navigationBarsPadding()
    ) {
        if (ratedItems.loadState.refresh is LoadState.Loading) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(10) {
                    LoadingMovieCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                }
            }
        }
        else if (ratedItems.itemCount == 0 && ratedItems.loadState.refresh !is LoadState.Loading) {
            StateMessage(
                imageDrawable =
                    if (Theme.isDark) com.cairosquad.design_system.R.drawable.favorite_list_empty_dark
                    else com.cairosquad.design_system.R.drawable.favorite_list_empty,
                titleId = R.string.no_ratings_yet,
                descriptionId = R.string.no_ratings_found_description,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                width = 180.dp,
                height = 150.dp,
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(
                    ratedItems.itemCount,
                    key = { index -> ratedItems[index]?.id ?: index }
                ) { index ->
                    ratedItems[index]?.let { item ->
                        RatedItemCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem(),
                            item = item,
                            onItemClick = onItemClick,
                            onMovieDelete = onMovieDelete,
                            onSeriesDelete = onSeriesDelete
                        )
                    }
                }
            }
        }
    }
}