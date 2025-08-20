package com.cairosquad.ui.more

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cairosquad.ui.more.content.MoreScreenContent
import com.cairosquad.ui.navigation.AppRoute
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.LoginRoute
import com.cairosquad.ui.navigation.MyRatingsRoute
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.viewmodel.more.MoreScreenEffect
import com.cairosquad.viewmodel.more.MoreViewModel
import kotlinx.coroutines.delay

@Composable
fun MoreScreen(
    viewModel: MoreViewModel = hiltViewModel(),
    setNavBarVisibility: (Boolean) -> Unit
) {
    val navController = LocalNavController.current
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    ObserveAsEffect(viewModel.effect) {
        when (it) {
            MoreScreenEffect.NavigateToLogin -> {
                navController.navigate(LoginRoute) {
                    popUpTo(AppRoute) {
                        inclusive = true
                    }
                }
            }

            MoreScreenEffect.NavigateToMyRatings -> {
                navController.navigate(MyRatingsRoute)
            }

        }
    }

    val isNavBarVisible =
        !(state.isThemeBottomSheetOpen
                || state.isLanguageBottomSheetOpen
                || state.isLogoutButtonVisible)

    LaunchedEffect(isNavBarVisible) {
        if (isNavBarVisible) delay(600)
        setNavBarVisibility(isNavBarVisible)
    }

    MoreScreenContent(
        state = state,
        listener = viewModel
    )
}