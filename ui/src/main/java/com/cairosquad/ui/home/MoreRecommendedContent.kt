package com.cairosquad.ui.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.viewmodel.home.HomeInteractionsListener
import com.cairosquad.viewmodel.home.HomeScreenState.MovieUiState
import com.cairosquad.viewmodel.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
fun MoreRecommendedContent(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    val navController = LocalNavController.current

    val state = homeViewModel.screenState.collectAsState()

    HomeSectionsContent(
        appBarTitle = com.cairosquad.ui.R.string.more_recommended,
        navController = navController,
        content = {
            RecommendedMoviesList(
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 16.dp)
                    .padding(horizontal = 16.dp),
                moreRecommendedMovie = state.value.moreRecommendedMovies,
                listener = homeViewModel
            )
        })
}

@Composable
private fun RecommendedMoviesList(
    moreRecommendedMovie:  List<MovieUiState>,
    listener: HomeInteractionsListener,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = moreRecommendedMovie.isEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
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

    AnimatedVisibility(
        visible = moreRecommendedMovie.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize(),
            columns = GridCells.Adaptive(minSize = 101.33.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(moreRecommendedMovie) { moreRecommended ->
                MovieCard(
                    modifier = Modifier.clickable {
                         listener.onClickMovie(moreRecommended.id)
                    },
                    title = moreRecommended.title,
                    vote = moreRecommended.rating,
                    imgUrl = moreRecommended.posterPath,
                    width = null,
                    aspectRatio = 0.743f
                )
            }
        }
    }
}

@Preview
@Composable
private fun MoreRecommendedScreenPrev() {
    MoreRecommendedContent()
}