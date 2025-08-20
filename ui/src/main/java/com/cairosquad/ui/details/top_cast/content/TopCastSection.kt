package com.cairosquad.ui.details.top_cast.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.ArtistCard
import com.cairosquad.ui.movio_component.LoadingArtistCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.viewmodel.details.top_cast.TopCastScreenState


@Composable
fun TopCastSection(
    state: TopCastScreenState,
    onClick: (Long) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 101.33.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        when (state.screenStatus) {
            TopCastScreenState.ScreenStatus.LOADING -> {
                items(20) {
                    LoadingArtistCard()
                }
            }

            TopCastScreenState.ScreenStatus.SUCCESS -> {
                items(state.cast) { artist ->
                    ArtistCard(
                        name = artist.name,
                        imgUrl = artist.photoPath,
                        modifier = Modifier.clickable { onClick(artist.id) }
                    )
                }
            }

            TopCastScreenState.ScreenStatus.ERROR -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        StateMessage(
                            imageDrawable =
                                if (Theme.isDark) R.drawable.no_internet_dark
                                else R.drawable.no_internet,
                            titleId = R.string.no_internet_connection,
                            descriptionId = R.string.internet_is_not_available_description
                        )
                    }
                }
            }
        }
    }
}
