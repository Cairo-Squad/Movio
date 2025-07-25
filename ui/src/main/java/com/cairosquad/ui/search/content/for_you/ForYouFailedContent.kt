package com.cairosquad.ui.search.content.for_you

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cairosquad.design_system.R
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.viewmodel.exception.ErrorStatus

@Composable
fun ForYouFailedContent(
    errorStatus: ErrorStatus?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (errorStatus) {
            ErrorStatus.NO_INTERNET -> StateMessage(
                imageDrawable = R.drawable.no_internet,
                titleId = R.string.no_internet_connection,
                descriptionId = R.string.no_internet_connection
            )

            else -> null
        }
    }
}