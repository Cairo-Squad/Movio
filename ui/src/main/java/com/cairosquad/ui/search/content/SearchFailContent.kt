package com.cairosquad.ui.search.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.cairosquad.design_system.component.InputField
import com.cairosquad.design_system.component.StateMessage
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.viewmodel.exception.ErrorStatus
import com.cairosquad.viewmodel.search.SearchInteractionListener
import com.cairosquad.viewmodel.search.SearchScreenState

@Composable
fun SearchFailContent(
    state: SearchScreenState,
    listener: SearchInteractionListener,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {

        InputField(
            modifier = Modifier
                .background(Theme.color.surfaces.surface)
                .padding(16.dp),
            value = state.query,
            onValueChange = listener::onQueryTextChanged,
            placeholder = stringResource(R.string.search),
            leadingIcon = R.drawable.search_bottom_nav,
            onFocusChanged = {
                if (it) {
                    listener.onClickSearchTextField()
                }
            },
            readOnly = true
        )

        Box(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.Center
        ) {
            StateMessage(
                imageDrawable = when (state.errorStatus) {
                    ErrorStatus.NO_INTERNET -> R.drawable.no_internet
                    ErrorStatus.NETWORK_ERROR -> R.drawable.no_result
                    ErrorStatus.UNKNOWN_ERROR -> R.drawable.no_result
                    null -> R.drawable.no_result
                },
                titleId = when (state.errorStatus) {
                    ErrorStatus.NO_INTERNET -> R.string.no_internet_connection
                    ErrorStatus.NETWORK_ERROR -> R.string.an_error_occured_while_getting_results
                    ErrorStatus.UNKNOWN_ERROR -> R.string.an_unexpected_error_occurred
                    null -> R.string.an_unexpected_error_occurred
                },
                descriptionId = when (state.errorStatus) {
                    ErrorStatus.NO_INTERNET -> R.string.internet_is_not_available_description
                    ErrorStatus.NETWORK_ERROR -> R.string.internet_is_not_available_description
                    ErrorStatus.UNKNOWN_ERROR -> R.string.an_unexpected_error_occurred_description
                    null -> R.string.an_unexpected_error_occurred_description
                }
            )
        }
    }
}