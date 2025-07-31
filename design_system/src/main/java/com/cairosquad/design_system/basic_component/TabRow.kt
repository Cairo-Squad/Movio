package com.cairosquad.design_system.basic_component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.theme.Theme

@Composable
fun TabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
) {
    val tabPositions = remember { mutableStateMapOf<Int, Pair<Int, Int>>() }
    var rowWidthPx by remember { mutableIntStateOf(0) }

    val layoutDirection = LocalLayoutDirection.current
    val density = LocalDensity.current

    val horizontalScrollingState = rememberScrollState()
    val horizontalScrollingValueDp = with(density) { horizontalScrollingState.value.toDp() }

    val indicatorOffsetX by animateDpAsState(
        targetValue = with(density) {
            tabPositions[selectedTabIndex]?.let { (xPx, widthPx) ->
                if (layoutDirection == LayoutDirection.Rtl) {
                    (rowWidthPx - xPx - widthPx).toDp()
                } else {
                    xPx.toDp()
                }
            } ?: 0.dp
        },
        animationSpec = tween(200)
    )

    val indicatorWidth by animateDpAsState(
        targetValue = with(density) {
            tabPositions[selectedTabIndex]?.second?.toDp() ?: 0.dp
        },
        animationSpec = tween(200)
    )

    val scrollThresholdPx = with(density) { 275.dp.toPx() }
    val scrollProgress = (scrollState.value / scrollThresholdPx).coerceIn(0f, 1f)

    val selectedTabTitle = tabs.getOrNull(selectedTabIndex)
    val indicatorBrush = if (selectedTabTitle == stringResource(com.cairosquad.design_system.R.string.categories)) {

        if (scrollProgress >= 0.5f) {
            Theme.color.gradiant.horizontalCategoriesGradient
        } else {
            Theme.color.gradiant.horizontalCategoriesGradient
        }
    } else {

        if (scrollProgress >= 0.5f) {
            Theme.color.gradiant.horizontalCategoriesGradient
        } else {
            Theme.color.gradiant.horizontalGradient
        }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .horizontalScroll(horizontalScrollingState)
                .onGloballyPositioned { rowWidthPx = it.size.width },
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTabIndex == index
                val isLightTheme = !isSystemInDarkTheme()

                val textColor by animateColorAsState(
                    targetValue = when {
                        isLightTheme && isSelected && scrollProgress > 0.5f ->
                            Theme.color.brand.onPrimaryContainer
                        isSelected ->
                            if (selectedTabTitle == stringResource(com.cairosquad.design_system.R.string.categories))
                                Theme.color.brand.onPrimaryContainer
                        else

                            Theme.color.brand.onPrimary
                        else ->
                            Theme.color.surfaces.onSurfaceVariant
                    },
                    animationSpec = tween(300)
                )

                val textStyle = if (isSelected)
                    Theme.textStyle.title.mediumMedium16
                else
                    Theme.textStyle.body.smallRegular16

                Column(
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onTabSelected(index) }
                        .padding(horizontal = 12.dp)
                        .onGloballyPositioned { coords ->
                            tabPositions[index] =
                                coords.positionInParent().x.toInt() to coords.size.width
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(7.5.dp)
                ) {
                    Text(
                        text = title,
                        color = textColor,
                        textAlign = TextAlign.Center,
                        style = textStyle
                    )
                }
            }
        }

        if (tabPositions.containsKey(selectedTabIndex)) {
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffsetX - horizontalScrollingValueDp)
                    .height(1.dp)
                    .width(indicatorWidth)
                    .align(Alignment.BottomStart)
                    .background(brush = indicatorBrush)
            )
        }
    }
}
