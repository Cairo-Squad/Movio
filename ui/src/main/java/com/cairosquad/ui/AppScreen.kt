package com.cairosquad.ui

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.cairosquad.design_system.R
import com.cairosquad.design_system.basic_component.BottomNavItem
import com.cairosquad.design_system.basic_component.NavigationBar
import com.cairosquad.design_system.basic_component.Scaffold
import com.cairosquad.ui.details.EpisodesScreen
import com.cairosquad.ui.home.HomeScreen
import com.cairosquad.ui.library.LibraryScreen
import com.cairosquad.ui.more.MoreScreen
import com.cairosquad.ui.search.SearchScreen

@Composable
fun AppScreen() {
    val navigationItems = remember {
        listOf(
            BottomNavItem(
                R.drawable.home_bottom_nav,
                R.drawable.home_bottom_nav_colored,
                R.string.home
            ),
            BottomNavItem(
                R.drawable.search_bottom_nav,
                R.drawable.search_bottom_nav_colored,
                R.string.search
            ),
            BottomNavItem(
                R.drawable.library_bottom_nav,
                R.drawable.library_bottom_nav_colored,
                R.string.library
            ),
            BottomNavItem(
                R.drawable.more_bottom_nav,
                R.drawable.more_bottom_nav_colored,
                R.string.more
            )
        )
    }
    var selectedScreenIndex by rememberSaveable { mutableIntStateOf(0) }

    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    DisposableEffect(backPressedDispatcher) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (selectedScreenIndex > 0) selectedScreenIndex--
                else this.remove()
            }
        }
        backPressedDispatcher?.addCallback(callback)
        onDispose {
            callback.remove()
        }
    }

    Scaffold(
        topBar = {},
        navBar = {
            NavigationBar(
                navigationItems = navigationItems,
                onMenuClick = {
                    selectedScreenIndex = it
                },
                selectedMenu = selectedScreenIndex
            )
        },
        content = {
            when (selectedScreenIndex) {
                0 -> HomeScreen()
                1 -> SearchScreen()
                2 -> LibraryScreen()
                3 -> MoreScreen()
                else -> HomeScreen()
            }
        })
}

@Preview
@Composable
fun AppScreenPreview() {
//    AppScreen()
}