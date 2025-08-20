package com.cairosquad.ui.rated.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.collectAsLazyPagingItems
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.rated.composable.RatedItemsList
import com.cairosquad.viewmodel.rated.MyRatingsInteractionListener
import com.cairosquad.viewmodel.rated.MyRatingsScreenState

@Composable
fun MyRatingsScreenContent(
    modifier: Modifier,
    state: MyRatingsScreenState,
    listener: MyRatingsInteractionListener
) {
    val ratedItems = state.ratedItems.collectAsLazyPagingItems()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Theme.color.surfaces.surface)
    ) {
        AppBar(
            onBackButtonClicked = {
                listener.onBackPressed()
            },
            title = stringResource(R.string.my_ratings),
            modifier = Modifier.statusBarsPadding()
        )
        RatedItemsList(
            ratedItems = ratedItems,
            onItemClick = { itemId, isMovie ->
                if (isMovie) {
                    listener.onMovieClick(itemId)
                } else {
                    listener.onSeriesClick(itemId)
                }
            },
            onMovieDelete = listener::onMovieDelete,
            onSeriesDelete = listener::onSeriesDelete
        )
    }
}