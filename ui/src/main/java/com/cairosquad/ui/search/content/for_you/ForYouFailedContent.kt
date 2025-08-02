package com.cairosquad.ui.search.content.for_you

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.Button
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.foryou.ForYouInteractionListener

@Composable
fun ForYouFailedContent(
    errorStatus: ErrorStatus?,
    listener: ForYouInteractionListener,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.weight(1f))
        when (errorStatus) {
            ErrorStatus.NO_INTERNET -> StateMessage(
                imageDrawable = R.drawable.no_internet,
                titleId = R.string.no_internet_connection,
                descriptionId = R.string.no_internet_connection
            )

            else -> null
        }
        Spacer(Modifier.weight(1f))
        if (errorStatus == ErrorStatus.NO_INTERNET) {
            Button(
                text = stringResource(R.string.try_again),
                onClick = { listener::onRefresh },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 32.dp)
                    .padding(horizontal = 16.dp)
            )
        }
    }
}