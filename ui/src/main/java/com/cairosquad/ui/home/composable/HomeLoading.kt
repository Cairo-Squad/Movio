package com.cairosquad.ui.home.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cairosquad.ui.utils.LoadingMoviesGrid

@Composable
fun HomeLoading(
    modifier: Modifier = Modifier,
) {
    LoadingMoviesGrid(
        modifier = modifier
            .statusBarsPadding()
            .padding(top = 48.dp)
            .padding(top = 36.dp)
    )
}