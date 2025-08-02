package com.cairosquad.ui.home.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.Button
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.home.HomeInteractionsListener

@Composable
fun HomeFailContent(
    errorStatus: ErrorStatus?,
    listener: HomeInteractionsListener,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.weight(1f))
        StateMessage(
            imageDrawable = when (errorStatus) {
                ErrorStatus.NO_INTERNET -> R.drawable.no_internet
                ErrorStatus.NETWORK_ERROR -> R.drawable.no_result
                ErrorStatus.UNKNOWN_ERROR -> R.drawable.no_result
                null -> R.drawable.no_result
                ErrorStatus.UNAUTHORIZED -> R.drawable.no_result
                ErrorStatus.EMPTY -> R.drawable.no_result
                ErrorStatus.PARSING_ERROR -> R.drawable.no_result
            },
            titleId = when (errorStatus) {
                ErrorStatus.NO_INTERNET -> R.string.no_internet_connection
                ErrorStatus.NETWORK_ERROR -> R.string.an_error_occured_while_getting_results
                ErrorStatus.UNKNOWN_ERROR -> R.string.an_unexpected_error_occurred
                null -> R.string.an_unexpected_error_occurred
                ErrorStatus.UNAUTHORIZED -> R.string.unauthorized_access
                ErrorStatus.EMPTY -> R.string.no_results_found
                ErrorStatus.PARSING_ERROR -> R.string.error_parsing_data
            },
            descriptionId = when (errorStatus) {
                ErrorStatus.NO_INTERNET -> R.string.internet_is_not_available_description
                ErrorStatus.NETWORK_ERROR -> R.string.internet_is_not_available_description
                ErrorStatus.UNKNOWN_ERROR -> R.string.an_unexpected_error_occurred_description
                null -> R.string.an_unexpected_error_occurred_description
                ErrorStatus.UNAUTHORIZED -> R.string.unauthorized_access
                ErrorStatus.EMPTY -> R.string.no_results_found
                ErrorStatus.PARSING_ERROR -> R.string.error_parsing_data
            }
        )
        Spacer(Modifier.weight(1f))
        if (errorStatus == ErrorStatus.NO_INTERNET) {
            Button(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 32.dp)
                    .padding(horizontal = 16.dp),
                text = stringResource(R.string.try_again),
                onClick = { listener::onRefresh }
            )

        }
    }
}
