package com.cairosquad.ui.search

import android.widget.Toast
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.cairosquad.ui.navigation.ArtistRoute
import com.cairosquad.ui.navigation.ForYouRoute
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.search.content.SearchScreenContent
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.ui.utils.errorStatusToMessageResource
import com.cairosquad.viewmodel.search.SearchEffect
import com.cairosquad.viewmodel.search.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinViewModel()
) {
    val context = LocalContext.current

    val navController = LocalNavController.current

    val keyboardController = LocalSoftwareKeyboardController.current

    val state by viewModel.screenState.collectAsState()

    ObserveAsEffect(viewModel.effect) { effect ->
        when (effect) {
            is SearchEffect.ErrorHappened -> {
                Toast.makeText(
                    context,
                    context.getString(errorStatusToMessageResource(effect.message)),
                    Toast.LENGTH_LONG
                ).show()
            }

            is SearchEffect.NavigateToArtistDetails -> {
                navController.navigate(ArtistRoute(effect.artistId))
            }

            is SearchEffect.NavigateToMovieDetails -> {
                navController.navigate(MovieRoute(effect.movieId))
            }

            SearchEffect.NavigateToSeeAllForYouScreen -> {
                navController.navigate(ForYouRoute)
            }

            is SearchEffect.NavigateToSeriesDetails -> {
                navController.navigate(SeriesRoute(effect.seriesId))
            }

            is SearchEffect.HideKeyboard -> {
                keyboardController?.hide()
            }
        }
    }

    SearchScreenContent(
        state = state,
        listener = viewModel,
        modifier = modifier.windowInsetsPadding(WindowInsets.statusBars)
    )
}