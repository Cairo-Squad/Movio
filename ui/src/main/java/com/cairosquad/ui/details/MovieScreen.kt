package com.cairosquad.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.Chip
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.ReviewsRoute
import com.cairosquad.ui.navigation.SimilarMovieRoute
import com.cairosquad.ui.navigation.TopCastRoute

@Composable
fun MovieScreen(
    movieId: Long
) {
    val navController = LocalNavController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
    ) {
        BasicText(
            text = "Movie with id: $movieId",
            style = Theme.textStyle.title.largeBold16
                .copy(color = Theme.color.surfaces.onSurface),
        )
        Chip(
            title = "back",
            onClick = { navController.popBackStack() }
        )
        Chip(
            title = "see all top cast for this movie",
            onClick = { navController.navigate(TopCastRoute(movieId, true)) }
        )
        Chip(
            title = "see reviews for this movie",
            onClick = { navController.navigate(ReviewsRoute(movieId, true)) }
        )
        Chip(
            title = "see similar movies for this movie",
            onClick = { navController.navigate(SimilarMovieRoute(movieId)) }
        )
    }

}