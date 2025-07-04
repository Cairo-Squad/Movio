package com.cairosquad.design_system.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.design_system.R


@Composable
fun NavigationBar(
    navigationItems: List<BottomNavItem>,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    selectedItemIndex: Int = 0,
) {
    Column(
        modifier = modifier
            .background(color = Theme.color.surfaces.surface)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(74.dp)

    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            navigationItems.forEachIndexed { index, item ->
                val iconResId =
                    if (selectedItemIndex == index) item.coloredIcon else item.unColoredIcon
                val labelColor =
                    if (selectedItemIndex == index) Theme.color.brand.primary else Theme.color.surfaces.onSurfaceVariant
                Column(
                    modifier = Modifier
                        .size(43.dp)
                        .clickable { onItemSelected(index) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .size(24.dp),
                        imageVector = ImageVector.vectorResource(id = iconResId),
                        contentDescription = "${item.label} icon",
                        tint = Color.Unspecified
                    )
                    Text(
                        text = item.label,
                        color = labelColor,
                        style = Theme.textStyle.label.smallRegular12
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewNavBarNightSelectedItem0() {
    PreviewMovioNavigationBar(isDarkTheme = true, selectedIndex = 0)
}

@Preview
@Composable
private fun PreviewNavBarLightSelectedItem0() {
    PreviewMovioNavigationBar(isDarkTheme = false, selectedIndex = 0)
}

@Preview
@Composable
private fun PreviewNavBarNightSelectedItem1() {
    PreviewMovioNavigationBar(isDarkTheme = true, selectedIndex = 1)
}

@Preview
@Composable
private fun PreviewNavBarLightSelectedItem1() {
    PreviewMovioNavigationBar(isDarkTheme = false, selectedIndex = 1)
}

@Preview
@Composable
private fun PreviewNavBarNightSelectedItem2() {
    PreviewMovioNavigationBar(isDarkTheme = true, selectedIndex = 2)
}

@Preview
@Composable
private fun PreviewNavBarLightSelectedItem2() {
    PreviewMovioNavigationBar(isDarkTheme = false, selectedIndex = 2)
}

@Preview
@Composable
private fun PreviewNavBarNightSelectedItem3() {
    PreviewMovioNavigationBar(isDarkTheme = true, selectedIndex = 3)
}

@Preview
@Composable
private fun PreviewNavBarLightSelectedItem3() {
    PreviewMovioNavigationBar(isDarkTheme = false, selectedIndex = 3)
}

@Preview(locale = "ar", name = "Arabic - Light Theme")
@Composable
private fun PreviewNavBarArabicLight() {
    PreviewMovioNavigationBar(
        isDarkTheme = false,
        selectedIndex = 0
    )
}

@Preview(locale = "ar", name = "Arabic - Dark Theme")
@Composable
private fun PreviewNavBarArabicDark() {
    PreviewMovioNavigationBar(
        isDarkTheme = true,
        selectedIndex = 0
    )
}

@Composable
private fun PreviewMovioNavigationBar(isDarkTheme: Boolean, selectedIndex: Int) {
    MovioTheme(isDarkTheme = isDarkTheme) {
        val navigationItems = listOf(
            BottomNavItem(
                R.drawable.home_bottom_nav, R.drawable.home_bottom_nav_colored,
                stringResource(R.string.home)
            ),
            BottomNavItem(
                R.drawable.search_bottom_nav, R.drawable.search_bottom_nav_colored,
                stringResource(R.string.search)
            ),
            BottomNavItem(
                R.drawable.library_bottom_nav,
                R.drawable.library_bottom_nav_colored,
                stringResource(R.string.library)
            ),
            BottomNavItem(
                R.drawable.more_bottom_nav, R.drawable.more_bottom_nav_colored,
                stringResource(R.string.more)
            )
        )
        NavigationBar(
            navigationItems = navigationItems,
            onItemSelected = {},
            selectedItemIndex = selectedIndex
        )
    }
}