package com.cairosquad.ui.details

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

@Composable
fun DetailsFailContent(
    onTryAgainClick: () -> Unit,
    modifier: Modifier = Modifier

) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        StateMessage(
            imageDrawable = R.drawable.no_internet,
            titleId = R.string.no_internet_connection,
            descriptionId = R.string.internet_is_not_available_description
        )
        Spacer(Modifier.weight(1f))
        Button(
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = 32.dp)
                .padding(horizontal = 16.dp),
            text = stringResource(R.string.try_again),
            onClick = onTryAgainClick
        )
    }
}