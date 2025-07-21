package com.cairosquad.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.cairosquad.ui.home.content.HomeScreenContent

@Composable
fun HomeScreen() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    HomeScreenContent(selectedTabIndex, { selectedTabIndex = it })
}

