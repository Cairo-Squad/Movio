package com.cairosquad.ui.home.content

import HomeCategoriesScreen
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cairosquad.ui.movio_component.CategoriesChips
import com.cairosquad.ui.movio_component.MediaSection
import com.cairosquad.ui.movio_component.MediaSectionItem
import com.cairosquad.ui.movio_component.MediaSectionLayoutType
import com.cairosquad.viewmodel.home.HomeInteractionsListener
import com.cairosquad.viewmodel.home.HomeScreenState

@Composable
fun HomeScreenSeeAllContent(
    screenState: HomeScreenState,
    listener: HomeInteractionsListener,
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
HomeCategoriesScreen()
//    val englishGenres = listOf(
//        "All",
//        "Action",
//        "Animation",
//        "Crime",
//        "Horror",
//        "Comedy",
//        "Romancy"
//    )
//    val options = listOf(
//        "All",
//        "Popularity",
//        "Latest"
//    )
//
//    var chipIndex by remember { mutableIntStateOf(0) }
//    var optionIndex by remember { mutableIntStateOf(0) }
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .verticalScroll(scrollState),
//    ) {
//        CategoriesChips(
//            modifier = Modifier.padding(top = 16.dp),
//            categories = englishGenres,
//            selectedChipIndex = chipIndex,
//            onChipSelected = { index -> chipIndex = index }
//        )
//        CategoriesChips(
//            modifier = Modifier.padding(top = 12.dp, bottom = 24.dp),
//            categories = options,
//            selectedChipIndex = optionIndex,
//            onChipSelected = { index -> optionIndex = index }
//        )
//        MediaSection(
//            modifier = Modifier.padding(bottom = 32.dp),
//            mediaList = screenState.topRatingMovies.map(MediaSectionItem::fromHomeMovieUiState),
//            onClickMedia = listener::onClickMovie,
//            mediaSectionLayoutType = MediaSectionLayoutType.LazyVerticalGrid(
//                minWidthDp = 101,
//                aspectRatio = 0.743f
//            )
//        )
//    }
}