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
import com.cairosquad.design_system.basic_component.Chip
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.ReviewsRoute
import com.cairosquad.ui.navigation.SeasonsRoute
import com.cairosquad.ui.navigation.SimilarSeriesRoute
import com.cairosquad.ui.navigation.TopCastRoute

@Composable
fun SeriesScreen(
    seriesId: Long
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
            text = "Series with id: $seriesId",
            style = Theme.textStyle.title.largeBold16
                .copy(color = Theme.color.surfaces.onSurface),
        )
        Chip(
            title = "see all top cast for this series",
            onClick = { navController.navigate(TopCastRoute(seriesId, false)) }
        )
        Chip(
            title = "back",
            onClick = { navController.popBackStack() }
        )
        Chip(
            title = "see current seasons for this series",
            onClick = { navController.navigate(SeasonsRoute(seriesId)) }
        )
        Chip(
            title = "see reviews for this series",
            onClick = { navController.navigate(ReviewsRoute(seriesId, false)) }
        )
        Chip(
            title = "see similar series for this series",
            onClick = { navController.navigate(SimilarSeriesRoute(seriesId)) }
        )
    }
}