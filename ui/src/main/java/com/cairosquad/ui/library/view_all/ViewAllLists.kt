package com.cairosquad.ui.library.view_all

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.Theme

@Composable
fun ViewAllLists() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("All Lists",
            style = Theme.textStyle.title.largeBold16,
            color = Theme.color.surfaces.onSurface
        )
    }
}