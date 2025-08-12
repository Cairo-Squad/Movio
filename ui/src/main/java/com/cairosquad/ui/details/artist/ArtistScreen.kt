package com.cairosquad.ui.details.artist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cairosquad.ui.details.artist.content.ArtistScreenContent
import com.cairosquad.ui.details.artist.content.ArtistScreenEffects
import com.cairosquad.viewmodel.details.artist.ArtistViewModel

@Composable
fun ArtistScreen(
    artistId: Long,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val artistViewModel: ArtistViewModel =
        hiltViewModel<ArtistViewModel, ArtistViewModel.Factory> { factory ->
            factory.create(artistId)
        }

    val state by artistViewModel.screenState.collectAsState()

    ArtistScreenEffects(artistViewModel, navController)

    ArtistScreenContent(modifier = modifier, state = state, listener = artistViewModel)
}