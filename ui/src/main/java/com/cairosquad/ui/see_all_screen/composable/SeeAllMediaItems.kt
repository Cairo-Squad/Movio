package com.cairosquad.ui.see_all_screen.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.see_all_screen.content.MediaGridContent
import com.cairosquad.ui.utils.LoadingMoviesGrid
import com.cairosquad.viewmodel.see_all.SeeAllInteractionsListener
import com.cairosquad.viewmodel.see_all.SeeAllScreenState

@Composable
fun SeeAllMediaItems(
    media: LazyPagingItems<SeeAllScreenState.MediaUiState>,
    state: SeeAllScreenState,
    listener: SeeAllInteractionsListener,
    modifier: Modifier = Modifier
) {

    when (state.screenStatus) {
        SeeAllScreenState.ScreenStatus.FAILED -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                StateMessage(
                    imageDrawable =
                        if (Theme.isDark) R.drawable.no_internet_dark
                        else R.drawable.no_internet,
                    titleId = R.string.no_internet_connection,
                    descriptionId = R.string.internet_is_not_available_description
                )
            }
        }
        SeeAllScreenState.ScreenStatus.LOADING -> {
            LoadingMoviesGrid(Modifier.padding(top = 16.dp))
        }
        SeeAllScreenState.ScreenStatus.Empty -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                StateMessage(
                    imageDrawable =
                        if (Theme.isDark) R.drawable.no_result_dark
                        else R.drawable.no_result,
                    titleId = R.string.no_results_found,
                    descriptionId = R.string.no_results_for_category
                )
            }
        }
        else -> {
            MediaGridContent(media, listener, modifier)
        }
    }
}