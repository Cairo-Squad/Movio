package com.cairosquad.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.movio_component.CategoriesChips
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.movio_component.TrendingMovieCard
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.viewmodel.home.HomeScreenState
import com.cairosquad.viewmodel.home.HomeViewModel
import com.cairosquad.viewmodel.search.SearchInteractionListener
import org.koin.androidx.compose.koinViewModel
import java.util.Locale
import kotlin.String

@Composable
fun TrendingContent(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    val navController = LocalNavController.current
    val englishGenres = listOf(
        "All",
        "Action",
        "Animation",
        "Crime",
        "Horror",
        "Comedy",
        "Romancy"
    )
    val state = homeViewModel.screenState.collectAsState()
    val chipIndex = remember { mutableStateOf(0) }
    val selectedGenre = englishGenres[chipIndex.value]
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Theme.color.surfaces.surface)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        AppBar(
            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
            title = "Trending",
            onBackButtonClicked = {
                navController.popBackStack()
            },
            onShareButtonClicked = null,
            onFavoriteButtonClicked = null
        )

        CategoriesChips(
            modifier = Modifier.padding(top = 16.dp),
            categories = englishGenres,
            selectedChipIndex = chipIndex.value,
            onChipSelected = { index ->
                chipIndex.value = index
            })
        MoviesList2(
            modifier = Modifier
                .padding(top = 24.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp),
            state = state.value,
            listener = null
        )

    }
    //   }
}

@Composable
private fun MoviesList2(
    state: HomeScreenState,
    listener: SearchInteractionListener?,
    modifier: Modifier = Modifier
) {
   val trendingMovies =state.trendingMovies
    AnimatedVisibility(
        visible = trendingMovies.isEmpty(),
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
        visible =trendingMovies.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            items(trendingMovies) { trendingMovie ->
                TrendingMovieCard(
                    imgUrl = trendingMovie.posterPath,
                    rating = String.format(Locale.getDefault(), "%.1f", trendingMovie.rating),
                    movieTitle = trendingMovie.title,
                    movieCategory = "Documentary"
                )
            }
        }
    }
}