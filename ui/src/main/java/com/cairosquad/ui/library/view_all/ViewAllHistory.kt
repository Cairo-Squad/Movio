package com.cairosquad.ui.library.view_all

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.modifier.dropShadow
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.TrendingMovieCard

@Composable
fun ViewAllHistory() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "All History",
            style = Theme.textStyle.title.largeBold16,
            color = Theme.color.surfaces.onSurface
        )
    }
}

@Composable
fun ViewAllHistoryContent(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.color.surfaces.surface)
    ) {
        Box(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .size(230.dp)
                .align(Alignment.TopEnd)
                .then(
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                        Modifier.dropShadow(
                            shape = CircleShape,
                            color = Theme.color.surfaces.onSurfaceAt5,
                            blur = 264.dp,
                            offsetX = 0.dp,
                            offsetY = 0.dp,
                            alpha = 0.10f
                        )
                    } else {
                        Modifier
                            .blur(264.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                            .background(
                                color = Theme.color.surfaces.onSurfaceAt5,
                                shape = CircleShape
                            )
                    }
                )
        )
        AppBar(
            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
            title = stringResource(R.string.history),
            onBackButtonClicked = { },
            onShareButtonClicked = null,
            onFavoriteButtonClicked = null
        )
        LazyColumn(
            modifier = Modifier.padding(top = 48.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(
                10
            ) {

                TrendingMovieCard(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    imgUrl = "",
                    movieTitle = "ASD",
                    movieCategory = "Action",
                    rating = "8.9"
                )
            }
            items(
                10
            ) {
                TrendingMovieCard(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    imgUrl = "",
                    movieTitle = "ASD",
                    movieCategory = "Action",
                    rating = "8.9"
                )
            }
        }
    }
}