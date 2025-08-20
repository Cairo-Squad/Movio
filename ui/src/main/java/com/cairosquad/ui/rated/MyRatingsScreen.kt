package com.cairosquad.ui.rated

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.cairosquad.design_system.basic_component.AppBar
import com.cairosquad.design_system.basic_component.SnackBar
import com.cairosquad.design_system.basic_component.Text
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.LoadingMovieCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.ui.navigation.MovieRoute
import com.cairosquad.ui.navigation.SeriesRoute
import com.cairosquad.ui.rated.composable.UndoRatingSnackBar
import com.cairosquad.ui.rated.content.MyRatingsScreenContent
import com.cairosquad.ui.utils.ObserveAsEffect
import com.cairosquad.ui.utils.getSnackBarIcon
import com.cairosquad.viewmodel.rated.MyRatingsEffect
import com.cairosquad.viewmodel.rated.MyRatingsInteractionListener
import com.cairosquad.viewmodel.rated.MyRatingsScreenState
import com.cairosquad.viewmodel.rated.MyRatingsViewModel

@Composable
fun MyRatingsScreen(
    modifier: Modifier = Modifier,
    viewModel: MyRatingsViewModel = hiltViewModel()
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val navController = LocalNavController.current
    ObserveAsEffect(viewModel.effect) {
        when (it) {
            MyRatingsEffect.NavigateBack -> {
                navController.popBackStack()
            }

            is MyRatingsEffect.NavigateToMovieDetails -> {
                navController.navigate(MovieRoute(it.movieId))
            }

            is MyRatingsEffect.NavigateToSeriesDetails -> {
                navController.navigate(SeriesRoute(it.seriesId))
            }
        }
    }
    Box {
        MyRatingsScreenContent(
            modifier = modifier,
            state = state,
            listener = viewModel
        )

        UndoRatingSnackBar(state, viewModel)
    }
}