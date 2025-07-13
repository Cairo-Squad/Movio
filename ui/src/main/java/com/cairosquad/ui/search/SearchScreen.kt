package com.cairosquad.ui.search

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.cairosquad.ui.search.content.SearchScreenContent
import com.cairosquad.ui.utils.ObserveAsEvent
import com.cairosquad.ui.utils.errorStatusToMessageResource
import com.cairosquad.viewmodel.search.SearchUiEvent
import com.cairosquad.viewmodel.search.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel(),
) {
    val context = LocalContext.current

    val state by viewModel.screenState.collectAsState()

    ObserveAsEvent(viewModel.uiEvent) { event ->
        when (event) {
            is SearchUiEvent.ErrorHappened -> {
                Toast.makeText(
                    context,
                    context.getString(errorStatusToMessageResource(event.errorStatus)),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    SearchScreenContent(
        state = state,
        listener = viewModel,
        modifier = modifier
    )
}

