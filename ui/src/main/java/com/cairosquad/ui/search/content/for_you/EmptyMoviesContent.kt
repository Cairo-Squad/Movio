package com.cairosquad.ui.search.content.for_you

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cairosquad.design_system.R
import com.cairosquad.ui.movio_component.StateMessage

@Composable
fun EmptyMoviesContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        StateMessage(
            imageDrawable = R.drawable.no_result,
            titleId = R.string.no_results_found,
            descriptionId = R.string.no_results_found_description
        )
    }
}