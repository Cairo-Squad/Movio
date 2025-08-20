package com.cairosquad.design_system.basic_component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Scaffold(
    topBar: @Composable () -> Unit,
    navBar: @Composable () -> Unit,
    isNavBarVisible: Boolean,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        topBar()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            content()
        }
        AnimatedVisibility(
            visible = isNavBarVisible,
            enter = slideInVertically(animationSpec = tween(100), initialOffsetY = { it }),
            exit = slideOutVertically(animationSpec = tween(100), targetOffsetY = { it }),
        ) { navBar() }
    }
}