package com.cairosquad.ui.home.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.ui.movio_component.CategoriesChips
import com.cairosquad.ui.movio_component.MediaSection
import com.cairosquad.ui.movio_component.MediaSectionItem
import com.cairosquad.ui.movio_component.MediaSectionLayoutType


@Composable
fun SeeAllContent(
    title: String,
    mediaList: List<MediaSectionItem>,
    onClickMedia: (Long) -> Unit,
    onClickBack: () -> Unit,
    modifier: Modifier = Modifier
){
    val englishGenres = listOf(
        "All",
        "Action",
        "Animation",
        "Crime",
        "Horror",
        "Comedy",
        "Romancy"
    )

    var chipIndex by remember { mutableIntStateOf(0) }


    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        AppBar(
            modifier = Modifier,
            title = title,
            onBackButtonClicked = onClickBack
        )
        CategoriesChips(
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            categories = englishGenres,
            selectedChipIndex = chipIndex,
            onChipSelected = { index -> chipIndex = index }
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            MediaSection(
                modifier = Modifier.padding(bottom = 32.dp),
                mediaList = mediaList,
                onClickMedia = onClickMedia,
                mediaSectionLayoutType = MediaSectionLayoutType.LazyVerticalGrid(
                    minWidthDp = 101,
                    aspectRatio = 0.743f
                )
            )
        }
    }
}
