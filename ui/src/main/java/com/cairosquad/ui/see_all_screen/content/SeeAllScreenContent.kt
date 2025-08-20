package com.cairosquad.ui.see_all_screen.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.RefreshBox
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.CategoriesChips
import com.cairosquad.ui.see_all_screen.composable.SeeAllMediaItems
import com.cairosquad.viewmodel.see_all.SeeAllInteractionsListener
import com.cairosquad.viewmodel.see_all.SeeAllScreenState
import com.cairosquad.viewmodel.util.MediaContentType

@Composable
fun SeeAllScreenContent(
    contentType: MediaContentType,
    media: LazyPagingItems<SeeAllScreenState.MediaUiState>,
    state: SeeAllScreenState,
    listener: SeeAllInteractionsListener,
    onBackAppBarClicked: () -> Unit
) {

    RefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { listener.onRefresh(state.selectedGenreIndex) },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.background(Theme.color.surfaces.surface)) {
            Box(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .align(Alignment.TopEnd)
                    .size(230.dp)
                    .blur(263.85.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                    .background(Theme.color.surfaces.onSurfaceAt5, shape = CircleShape)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                AppBar(
                    modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
                    title = stringResource(contentType.titleId),
                    onBackButtonClicked = onBackAppBarClicked,
                    onShareButtonClicked = null,
                    onFavoriteButtonClicked = null
                )

                CategoriesChips(
                    modifier = Modifier.padding(top = 16.dp),
                    categories = state.genres.map { genre ->
                        if (genre.id == null) {
                            stringResource(com.cairosquad.ui.R.string.all)
                        } else {
                            genre.name
                        }
                    },
                    selectedChipIndex = state.selectedGenreIndex,
                    onChipSelected = { index ->
                        listener.onGenreSelected(index)
                    })

                if (contentType != MediaContentType.TRENDING) {
                    SeeAllMediaItems(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .padding(horizontal = 16.dp),
                        state = state,
                        listener = listener,
                        media = media
                    )
                } else {
                    TrendingListContent(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .padding(horizontal = 16.dp),
                        listener = listener,
                        state = state,
                        media = media
                    )
                }
            }
        }
    }
}