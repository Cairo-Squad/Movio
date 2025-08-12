package com.cairosquad.ui.details.top_cast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cairosquad.ui.details.top_cast.content.TopCastScreenContent
import com.cairosquad.ui.navigation.ArtistRoute
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

    TopCastScreenContent(
        onBackClick = { navController.popBackStack() },
        onClick = { artistId -> navController.navigate(ArtistRoute(artistId)) },
        state = state
    )
}