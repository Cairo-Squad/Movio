package com.cairosquad.ui.search

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.component.InputField
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.domain.search.usecase.SearchUseCase
import com.cairosquad.viewmodel.searchviewmodel.SearchUiState.MovieUiState

import org.koin.compose.getKoin

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    useCase: SearchUseCase = getKoin().get()
) {
    var searchQuery by remember { mutableStateOf("") }
    var movies by remember { mutableStateOf(emptyList<MovieUiState>()) }

    LaunchedEffect(searchQuery) {
        val movieList = useCase.searchMovies(searchQuery)
        movies = movieList.map {
            MovieUiState(
                id = it.id,
                title = it.title,
                posterPath = "https://image.tmdb.org/t/p/w500${it.posterPath}",
                rating = it.rating
            )
        }
        Log.d("Test asd", "SearchScreen: $movies")
    }

    Column(
        modifier = modifier
            .background(Theme.color.surfaces.surface)
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        InputField(
            modifier = Modifier.padding(16.dp),
            value = searchQuery,
            onValueChange = {
                searchQuery = it
            },
            placeholder = stringResource(R.string.search),
            leadingIcon = R.drawable.search_bottom_nav,
        )
        IdleSearchScreenContent(
            movies,
            movies,
        ) {}
    }
}