package com.cairosquad.design_system.basic_component

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.modifier.dropShadow
import com.cairosquad.design_system.theme.MovioTheme
import com.cairosquad.design_system.theme.Theme


@Composable
fun NavigationBar(
    onMenuClick: (Int) -> Unit,
    navigationItems: List<BottomNavItem>,
    modifier: Modifier = Modifier,
    selectedMenu: Int = 0,
) {

    Row(
        modifier = modifier
            .dropShadow(
                shape = RectangleShape,
                color = Theme.color.system.dropShadow,
                offsetX = 0.dp,
                offsetY = 1.dp,
                blur = 12.dp,
                spread = 0.dp,
                alpha = 0.06f
            )
            .background(color = Theme.color.surfaces.surface)
            .height(74.dp)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        navigationItems.forEachIndexed { index, item ->
            val iconResId = if (selectedMenu == index) item.coloredIcon else item.unColoredIcon


            val labelColor by animateColorAsState(
                targetValue = if (selectedMenu == index)
                    Theme.color.brand.primary
                else
                    Theme.color.surfaces.onSurfaceVariant,
                animationSpec = tween(450, easing = EaseInOut)
            )
            Column(
                modifier = Modifier
                    .size(height = 43.dp, width = 74.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onMenuClick(index) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedContent(
                    targetState = iconResId,
                    transitionSpec = {
                        (fadeIn(tween(450))).togetherWith(fadeOut(tween(450)))
                    },
                    label = "Icon Animation"
                ) { iconResId ->
                    Icon(
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .size(24.dp),
                        imageVector = ImageVector.vectorResource(id = iconResId),
                        contentDescription = "${stringResource(item.labelId)} ${stringResource(R.string.icon)}",
                        tint = Color.Unspecified
                    )
                }
                Text(
                    text = stringResource(item.labelId),
                    color = labelColor,
                    style = Theme.textStyle.label.smallRegular12
                )
            }
        }
    }
}

@SuppressLint("SupportAnnotationUsage")
data class BottomNavItem(
    @DrawableRes val unColoredIcon: Int,
    @DrawableRes val coloredIcon: Int,
    @StringRes val labelId: Int
)

@Preview
@Composable
private fun PreviewNavBarNightSelectedItem0() {
    PreviewMovioNavigationBar(isDarkTheme = true)
}

@Preview
@Composable
private fun PreviewNavBarLightSelectedItem0() {
    PreviewMovioNavigationBar(isDarkTheme = false)
}

@Preview(locale = "ar", name = "Arabic - Light Theme")
@Composable
private fun PreviewNavBarArabicLight() {
    PreviewMovioNavigationBar(
        isDarkTheme = false,
    )
}

@Preview(locale = "ar", name = "Arabic - Dark Theme")
@Composable
private fun PreviewNavBarArabicDark() {
    PreviewMovioNavigationBar(
        isDarkTheme = true,

        )
}

@Composable
private fun PreviewMovioNavigationBar(isDarkTheme: Boolean) {
    MovioTheme(isDarkTheme = isDarkTheme) {
        var selectedMenu by remember { mutableIntStateOf(0) }
        NavigationBar(
            onMenuClick = {
                selectedMenu = it
            },
            navigationItems = listOf(
                BottomNavItem(
                    R.drawable.home_bottom_nav, R.drawable.home_bottom_nav_colored,
                    R.string.home
                ),
                BottomNavItem(
                    R.drawable.search_bottom_nav, R.drawable.search_bottom_nav_colored,
                    R.string.search
                ),
                BottomNavItem(
                    R.drawable.library_bottom_nav,
                    R.drawable.library_bottom_nav_colored,
                    R.string.library
                ),
                BottomNavItem(
                    R.drawable.more_bottom_nav, R.drawable.more_bottom_nav_colored,
                    R.string.more
                )
            ),
            selectedMenu = selectedMenu
        )
    }
}