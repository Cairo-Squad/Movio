package com.cairosquad.ui.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.ui.movio_component.ArtistCard
import com.cairosquad.ui.navigation.ArtistRoute
import com.cairosquad.viewmodel.details.top_cast.TopCastScreenState
import com.cairosquad.viewmodel.details.top_cast.TopCastViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import org.koin.core.parameter.parametersOf

@Composable
fun TopCastScreen(
    mediaId: Long,
    isMovie: Boolean,
    navController: NavHostController,
    viewmodel: TopCastViewModel = koinViewModel<TopCastViewModel>(
        parameters = { parametersOf(mediaId, isMovie) }
    )
) {
    val state by viewmodel.screenState.collectAsState()

    TopCastContent(
        onBackClick = { navController.popBackStack() },
        onClick = { artistId -> navController.navigate(ArtistRoute(artistId)) },
        cast = state.cast
    )
}

@Composable
private fun TopCastContent(
    onBackClick: () -> Unit,
    onClick: (Long) -> Unit,
    cast: List<TopCastScreenState.ArtistUiState>
) {
    Column (
        modifier = Modifier.fillMaxSize().systemBarsPadding()
    ){
        AppBar(
            title = stringResource(R.string.top_cast),
            onBackButtonClicked = onBackClick,
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 101.33.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cast) { artist ->
                ArtistCard(
                    name = artist.name,
                    imgUrl = artist.photoPath,
                    modifier = Modifier.clickable { onClick(artist.id) }
                )

            }
        }
    }
}