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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.viewmodel.home.HomeInteractionsListener
import com.cairosquad.viewmodel.home.HomeScreenState
import com.cairosquad.viewmodel.home.HomeScreenState.MovieUiState
import com.cairosquad.viewmodel.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoreRecommendedScreen(modifier: Modifier = Modifier) {
    val homeViewModel: HomeViewModel = koinViewModel()
    val state by homeViewModel.screenState.collectAsState()
val strategy=MoreRecommendedStrategy(mediaType = MediaType.Movies)
    DiscoverScreen(
        discoverContentStrategy = strategy,
        homeViewModel = homeViewModel
    ) {
        RecommendedMoviesList(
            modifier = Modifier
                .padding(top = 24.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp),
            discoverContentStrategy = strategy,
            state = state,
            listener = homeViewModel
        )

    }
}


@Composable
private fun RecommendedMoviesList(
    discoverContentStrategy: DiscoverContentStrategy,
    state: HomeScreenState,
    listener: HomeInteractionsListener,
    modifier: Modifier = Modifier
) {
    val items =   discoverContentStrategy.getItems(state).filterIsInstance<HomeScreenState.MovieUiState>()
    AnimatedVisibility(
        visible = items.isEmpty(),
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
        visible = items.isNotEmpty(),
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
            items(items) { moreRecommended ->
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