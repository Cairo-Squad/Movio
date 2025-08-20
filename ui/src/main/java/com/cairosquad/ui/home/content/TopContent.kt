package com.cairosquad.ui.home.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.stringResource
import com.cairosquad.design_system.basic_component.TabRow
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.AppBar
import com.cairosquad.viewmodel.home.HomeInteractionsListener
import com.cairosquad.viewmodel.home.HomeScreenState
import com.cairosquad.viewmodel.home.HomeScreenState.Tab.CATEGORIES

@Composable
fun TopContent(
    screenState: HomeScreenState,
    listener: HomeInteractionsListener,
    scrollProgress: Float,
    modifier: Modifier = Modifier
) {
    val topColor = lerp(
        if (Theme.isDark) Color.Black else Color.White,
        Theme.color.surfaces.surface,
        scrollProgress
    )

    val bottomColor = lerp(
        if (screenState.selectedTab == CATEGORIES)
            Theme.color.surfaces.surface
        else
            Color.Transparent,
        Theme.color.surfaces.surface,
        scrollProgress
    )

    val animatedBrush = Brush.verticalGradient(
        colors = listOf(topColor, bottomColor)
    )

    val tabsNamesResId = remember {
        listOf(
            R.string.movies,
            R.string.tv_shows,
            R.string.categories,
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(animatedBrush)
    ) {
        AppBar(
            modifier = Modifier.statusBarsPadding(),
            profileImage = screenState.profileImage,
            onClickProfileImage = listener::onProfileClick
        )

        TabRow(
            modifier = Modifier,
            tabs = tabsNamesResId.map { stringResource(it) },
            selectedTabIndex = screenState.selectedTab.ordinal,
            onTabSelected = listener::onTabClick,
            scrollProgress = scrollProgress,
            tabColorWithScroll = Theme.color.brand.onPrimaryContainer,
            tabColorWithNoScroll = Theme.color.brand.onPrimary,
            indicatorColorWithScroll = Theme.color.gradiant.horizontalCategoriesGradient,
            indicatorColorWithNoScroll = Theme.color.gradiant.horizontalGradient
        )
    }
}