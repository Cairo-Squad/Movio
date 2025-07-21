package com.cairosquad.ui.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.viewmodel.home.HomeInteractionsListener
import com.cairosquad.viewmodel.home.HomeScreenState.MovieUiState
import com.cairosquad.viewmodel.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
fun TopRatingContent(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    val topRating = homeViewModel.screenState.collectAsState()
    val navController = LocalNavController.current
    HomeSectionsContent(
        appBarTitle = R.string.top_rating,
        navController = navController,
        content = {
            TopRatingMoviesList(
                content = {},
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 16.dp)
                    .padding(horizontal = 16.dp),
                topRatingSeries = topRating.value.topRatingMovies,
                listener = homeViewModel
            )
        })
}

@Composable
fun TopRatingMoviesList(
    content: @Composable () -> Unit,
    topRatingSeries: List<MovieUiState>,
    listener: HomeInteractionsListener,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = topRatingSeries.isEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            StateMessage(
                imageDrawable = com.cairosquad.design_system.R.drawable.no_result,
                titleId = com.cairosquad.design_system.R.string.no_results_found,
                descriptionId = com.cairosquad.design_system.R.string.no_results_found_description
            )
        }
    }
    val scrollState = rememberScrollState()
    AnimatedVisibility(
        visible = topRatingSeries.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            item {
                Column {
                    content()
                }
            }
            item {
            LazyVerticalGrid(
                modifier = modifier
                    .heightIn(max = 10000.dp)
                    .fillMaxWidth(),
                columns = GridCells.Adaptive(minSize = 101.33.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                userScrollEnabled = false
            ) {
                items(topRatingSeries) { moreRecommended ->
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
            }}
        }
    }
}