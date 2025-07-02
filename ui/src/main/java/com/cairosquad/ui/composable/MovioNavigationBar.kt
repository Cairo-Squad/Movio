package com.cairosquad.ui.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R

@Composable
fun MovioNavigationBar(
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    selectedItemIndex: Int = 0,
) {
    val navigationItems = listOf(
        BottomNavItem(R.drawable.home_bottom_nav, R.drawable.home_bottom_nav_colored, "Home"),
        BottomNavItem(R.drawable.search_bottom_nav, R.drawable.search_bottom_nav_colored, "Search"),
        BottomNavItem(
            R.drawable.library_bottom_nav,
            R.drawable.library_bottom_nav_colored,
            "Library"
        ),
        BottomNavItem(R.drawable.more_bottom_nav, R.drawable.more_bottom_nav_colored, "More")
    )

    Box(
        modifier = modifier
            .background(color = Theme.color.surfaces.surface)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .fillMaxWidth()
            .height(74.dp)
            .padding(horizontal = 20.dp, vertical = 15.5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            navigationItems.forEachIndexed { index, item ->
                val iconResId =
                    if (selectedItemIndex == index) item.coloredIcon else item.unColoredIcon
                Box(
                    modifier = Modifier
                        .size(43.dp)
                        .clickable { onItemSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .size(24.dp)
                            .padding(bottom = 4.dp),
                        imageVector = ImageVector.vectorResource(id = iconResId),
                        contentDescription = "${item.label} icon",
                        tint = Color.Unspecified
                    )
                    Text(
                        text = item.label,
                        modifier = Modifier.align(Alignment.BottomCenter),
                        color = Theme.color.surfaces.onSurfaceVariant,
                        style = Theme.textStyle.label.smallRegular12
                    )
                }
            }
        }
    }
}

data class BottomNavItem(
    @DrawableRes val unColoredIcon: Int,
    @DrawableRes val coloredIcon: Int,
    val label: String
)


@Preview("MovioNavigationBarDark")
@Composable
fun MovioNavigationBarPreviewDark() {
    MovioTheme(isDarkTheme = true) {
        MovioNavigationBar(onItemSelected = {})
    }
}

@Preview("MovioNavigationBarLight")
@Composable
fun MovioNavigationBarPreviewLight() {
    MovioTheme(isDarkTheme = false) {
        MovioNavigationBar(onItemSelected = {})
    }
}

@Preview("MovioNavigationBarDark")
@Composable
fun MovioNavigationBarPreviewDark2() {
    MovioTheme(isDarkTheme = true) {
        MovioNavigationBar(onItemSelected = {}, selectedItemIndex = 1)
    }
}

@Preview("MovioNavigationBarLight")
@Composable
fun MovioNavigationBarPreviewLight2() {
    MovioTheme(isDarkTheme = false) {
        MovioNavigationBar(onItemSelected = {}, selectedItemIndex = 1)
    }
}

@Preview("MovioNavigationBarDark")
@Composable
fun MovioNavigationBarPreviewDark3() {
    MovioTheme(isDarkTheme = true) {
        MovioNavigationBar(onItemSelected = {}, selectedItemIndex = 2)
    }
}

@Preview("MovioNavigationBarLight")
@Composable
fun MovioNavigationBarPreviewLight3() {
    MovioTheme(isDarkTheme = false) {
        MovioNavigationBar(onItemSelected = {}, selectedItemIndex = 2)
    }
}

@Preview("MovioNavigationBarDark")
@Composable
fun MovioNavigationBarPreviewDark4() {
    MovioTheme(isDarkTheme = true) {
        MovioNavigationBar(onItemSelected = {}, selectedItemIndex = 3)
    }
}

@Preview("MovioNavigationBarLight")
@Composable
fun MovioNavigationBarPreviewLight4() {
    MovioTheme(isDarkTheme = false) {
        MovioNavigationBar(onItemSelected = {}, selectedItemIndex = 3)
    }
}


