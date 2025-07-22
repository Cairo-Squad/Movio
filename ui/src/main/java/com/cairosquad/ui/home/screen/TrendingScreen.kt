import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cairosquad.design_system.R
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.ui.movio_component.TrendingMovieCard
import com.cairosquad.ui.navigation.LocalNavController
import com.cairosquad.viewmodel.home.HomeScreenState
import com.cairosquad.viewmodel.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.Locale


@Composable
fun TrendingScreen(
    modifier: Modifier = Modifier,
) {

    val homeViewModel: HomeViewModel = koinViewModel()
    val state by homeViewModel.screenState.collectAsState()
    val strategy = TrendingStrategy(mediaType = MediaType.Movies)

    val navController = LocalNavController.current
    DiscoverScreen(
        discoverContentStrategy = strategy,
        navController = navController,
        homeViewModel = homeViewModel
    ) {
        TrendingContentList(
            modifier = Modifier
                .padding(top = 24.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp),
            discoverContentStrategy = strategy,
            state = state,
        )

    }


}

@Composable
private fun TrendingContentList(
    discoverContentStrategy: DiscoverContentStrategy,
    state: HomeScreenState,
    modifier: Modifier = Modifier
) {
    val trendingMovies =
        discoverContentStrategy.getItems(state).filterIsInstance<HomeScreenState.MovieUiState>()
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
        visible = trendingMovies.isNotEmpty(),
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