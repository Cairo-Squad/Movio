package com.cairosquad.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.ArtistCard
import com.cairosquad.ui.movio_component.LoadingArtistCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.navigation.ArtistRoute
import com.cairosquad.viewmodel.details.top_cast.TopCastScreenState
import com.cairosquad.viewmodel.details.top_cast.TopCastViewModel
import kotlinx.coroutines.Dispatchers

@Composable
fun TopCastScreen(
    mediaId: Long,
    isMovie: Boolean,
    navController: NavHostController,
) {
    val viewmodel: TopCastViewModel =
        hiltViewModel<TopCastViewModel, TopCastViewModel.Factory> { factory ->
            factory.create(
                mediaId = mediaId,
                isMovie = isMovie,
                dispatcher = Dispatchers.IO
            )
        }

    val state by viewmodel.screenState.collectAsState()

    TopCastContent(
        onBackClick = { navController.popBackStack() },
        onClick = { artistId -> navController.navigate(ArtistRoute(artistId)) },
        state = state
    )
}

@Composable
private fun TopCastContent(
    onBackClick: () -> Unit,
    onClick: (Long) -> Unit,
    state: TopCastScreenState
) {
    Box {
        Box(
            Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .align(Alignment.TopEnd)
                .blur(264.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                .size(230.dp)
                .background(Theme.color.surfaces.onSurfaceAt5)
        )

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(top = 56.dp),
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
                                imageDrawable = R.drawable.no_internet,
                                titleId = R.string.no_internet_connection,
                                descriptionId = R.string.internet_is_not_available_description
                            )
                        }
                    }
                }
            }

        }
    }
    AppBar(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.statusBars),
        title = stringResource(R.string.top_cast),
        onBackButtonClicked = onBackClick,
    )
}