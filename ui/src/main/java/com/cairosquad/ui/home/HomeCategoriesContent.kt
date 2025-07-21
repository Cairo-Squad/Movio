package com.cairosquad.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.TabRow
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.CategoriesChips
import com.cairosquad.viewmodel.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeCategoriesContent(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = koinViewModel()
) {
    val state = homeViewModel.screenState.collectAsState()
    val englishGenres = listOf(
        "All",
        "Action",
        "Animation",
        "Crime",
        "Horror",
        "Comedy",
        "Romancy"
    )
    val options = listOf(
        "All",
        "Popularity",
        "Latest",

        )
    val chipIndex = remember { mutableStateOf(0) }
    val optionIndex = remember { mutableStateOf(0) }
    val selectedGenre = englishGenres[chipIndex.value]
    Box(modifier = modifier.background(Theme.color.surfaces.surface)) {
        Box(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .align(Alignment.TopEnd)
                .size(230.dp)
                .blur(263.85.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                .background(Color(0x33734EF8), shape = CircleShape)
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            com.cairosquad.ui.movio_component.AppBar(modifier = Modifier.statusBarsPadding())
            TabRow(
                modifier = Modifier,
                tabs = listOf(
                    stringResource(R.string.all),
                    stringResource(R.string.movies),
                    stringResource(R.string.tv_shows),
                    stringResource(R.string.categories),
                ),
                selectedTabIndex = state.value.selectedTab.ordinal,
                onTabSelected = homeViewModel::onClickTab
            )
            TopRatingMoviesList(topRatingSeries = state.value.topRatingMovies, listener = homeViewModel, content = {
                CategoriesChips(
                    modifier = Modifier.padding(top = 16.dp),
                    categories = englishGenres,
                    selectedChipIndex = chipIndex.value,
                    onChipSelected = { index ->
                        chipIndex.value = index
                    })
                CategoriesChips(
                    modifier = Modifier.padding(top = 12.dp, bottom = 24.dp),
                    categories = options,
                    selectedChipIndex = optionIndex.value,
                    onChipSelected = { index ->
                        optionIndex.value = index
                    })
            })

        }
    }
}