package com.cairosquad.ui.library.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.details.composable.BlurredCircle
import com.cairosquad.ui.library.composable.HistoryItemsContent
import com.cairosquad.ui.library.composable.HistoryItemsEmpty
import com.cairosquad.viewmodel.library.view_all_history.ViewAllHistoryInteractionListener
import com.cairosquad.viewmodel.library.view_all_history.ViewAllHistoryScreenState

@Composable
fun ViewAllHistoryContent(
    screenState: ViewAllHistoryScreenState,
    listener: ViewAllHistoryInteractionListener
) {

    val movies = screenState.movies
    val series = screenState.series

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surfaces.surface)
    ) {
        BlurredCircle()
        Column {
            AppBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
                title = stringResource(R.string.history),
                onBackButtonClicked = listener::onBackClick,
                onShareButtonClicked = null,
                onFavoriteButtonClicked = null
            )
            when (screenState.screenStatus) {
                ViewAllHistoryScreenState.SectionStatus.LOADING -> {

                }

                ViewAllHistoryScreenState.SectionStatus.SUCCESS -> {
                    if (movies.isNotEmpty() || series.isNotEmpty()) {
                        HistoryItemsContent(movies, listener, series)
                    } else {
                        HistoryItemsEmpty()
                    }
                }

                ViewAllHistoryScreenState.SectionStatus.ERROR -> {

                }
            }
        }
    }
}

