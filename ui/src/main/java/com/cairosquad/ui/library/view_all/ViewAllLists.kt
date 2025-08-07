package com.cairosquad.ui.library.view_all

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.Icon
import com.cairosquad.design_system.modifier.dropShadow
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.library.component.ListContainer

@Composable
fun ViewAllLists() {
    ViewAllListsContent()
}

@Composable
private fun ViewAllListsContent(modifier: Modifier = Modifier) {
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
            title = "Watch Later",
            onBackButtonClicked = { },
            onShareButtonClicked = null,
            onFavoriteButtonClicked = null
        )
        LazyVerticalGrid(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = 48.dp),
            columns = GridCells.Adaptive(minSize = 158.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(
                10,
                key = { it -> it }
            ) {
                ListContainer(
                    listName = "Action",
                    numberOfItems = 10,
                    onListClicked = {}
                )
            }
            items(
                5,
                key = { it -> it }
            ) {
                ListContainer(
                    listName = "Action",
                    numberOfItems = 10,
                    onListClicked = {}
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(vertical = 32.dp, horizontal = 24.dp)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .size(60.dp)
                .clip(CircleShape)
                .background(Theme.color.brand.primary)
                .clickable(onClick = {}),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                contentDescription = "Add Icon",
                tint = Theme.color.brand.onPrimary
            )
        }
    }
}

@Preview
@Composable
private fun ViewAllListsPreview(modifier: Modifier = Modifier) {
    MovioTheme(isDarkTheme = true) {
        ViewAllListsContent()
    }
}