package com.cairosquad.ui.library.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.Button
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.library.composable.FavoriteSection
import com.cairosquad.ui.library.composable.HistorySection
import com.cairosquad.ui.library.composable.ListsSection
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.viewmodel.library.LibraryInteractionListener
import com.cairosquad.viewmodel.library.LibraryScreenState

@Composable
fun LibraryScreenContent(
    screenState: LibraryScreenState,
    listener: LibraryInteractionListener
) {

    Column {
        AppBar(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .background(Theme.color.surfaces.surface),
            title = stringResource(com.cairosquad.ui.R.string.library)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.color.surfaces.surface)
        ) {
            when (screenState.screenStatus) {
                LibraryScreenState.SectionStatus.LOADING -> {}
                LibraryScreenState.SectionStatus.SUCCESS -> {
                    if (screenState.isUserAuthed) {
                        item { ListsSection(listener, screenState) }
                        item { FavoriteSection(screenState, listener) }
                        item { HistorySection(screenState, listener) }
                    }
                }

                LibraryScreenState.SectionStatus.ERROR -> {
                    item {
                        LibraryFailContent(
                            screenState.errorStatus,
                            listener = listener
                        )
                    }
                }
            }
        }
    }
    when (screenState.screenStatus) {
        LibraryScreenState.SectionStatus.LOADING -> {}
        LibraryScreenState.SectionStatus.SUCCESS -> {
            if (!screenState.isUserAuthed) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 16.dp)
                        .padding(top = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    StateMessage(
                        imageDrawable = R.drawable.logo,
                        titleId = com.cairosquad.ui.R.string.log_in_to_unlock_your_personal_library,
                        descriptionId = com.cairosquad.ui.R.string.access_your_watch_history_favorites_and_watchlist_all_in_one_place,
                        width = 80.dp,
                        height = 88.dp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp),
                        text = stringResource(com.cairosquad.ui.R.string.login),
                        onClick = listener::onLoginClick
                    )
                }
            }
        }

        LibraryScreenState.SectionStatus.ERROR -> {}
    }
}