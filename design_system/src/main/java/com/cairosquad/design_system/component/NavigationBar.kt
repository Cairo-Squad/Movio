package com.cairosquad.design_system.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme

@Composable
fun NavigationBar(
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(color = Theme.color.surfaces.surface)
            .fillMaxWidth()
            .height(74.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        navigationItems.forEachIndexed { index, navigationItem ->
            val iconResId =
                if (selectedItemIndex == index) navigationItem.coloredIcon
                else navigationItem.unColoredIcon
            Column(
                modifier = Modifier
                    .width(74.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onItemSelected(index) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .size(24.dp),
                    imageVector = ImageVector.vectorResource(id = iconResId),
                    contentDescription = "${stringResource(navigationItem.label)} icon",
                    tint = Color.Unspecified
                )
                Text(
                    text = stringResource(navigationItem.label),
                    color = Theme.color.surfaces.onSurfaceVariant,
                    style = Theme.textStyle.label.smallRegular12
                )
            }
        }
    }
}

private val navigationItems = listOf(
    BottomNavItem(
        R.drawable.home_bottom_nav,
        R.drawable.home_bottom_nav_colored,
        R.string.home
    ),
    BottomNavItem(
        R.drawable.search_bottom_nav,
        R.drawable.search_bottom_nav_colored,
        R.string.search
    ),
    BottomNavItem(
        R.drawable.library_bottom_nav,
        R.drawable.library_bottom_nav_colored,
        R.string.library
    ),
    BottomNavItem(
        R.drawable.more_bottom_nav,
        R.drawable.more_bottom_nav_colored,
        R.string.more
    )
)

private data class BottomNavItem(
    @DrawableRes val unColoredIcon: Int,
    @DrawableRes val coloredIcon: Int,
    @StringRes val label: Int
)

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
        NavigationBar(
            onItemSelected = { },
            selectedItemIndex = selectedIndex
        )
    }
}