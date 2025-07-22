import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.ui.movio_component.StateMessage
import com.cairosquad.viewmodel.home.HomeInteractionsListener
import com.cairosquad.viewmodel.home.HomeScreenState.MovieUiState
import com.cairosquad.viewmodel.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue

@Composable
fun HomeCategoriesScreen(
    modifier: Modifier = Modifier,

    ) {
    val homeViewModel: HomeViewModel = koinViewModel()
    val state by homeViewModel.screenState.collectAsState()
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
                selectedTabIndex = state.selectedTab.ordinal,
                onTabSelected = homeViewModel::onClickTab
            )
            TopRatingMoviesList(
                topRatingSeries = state.topRatingMovies, listener = homeViewModel, content = {
                    CategoriesChips(
                        modifier = Modifier.padding(top = 16.dp),
                        categories = state.genres,
                        selectedChipIndex = state.selectedCategoriesChip,
                        onChipSelected = { index ->
                            homeViewModel.onClickCategoryChip(index)
                        })
                    CategoriesChips(
                        modifier = Modifier.padding(top = 12.dp, bottom = 24.dp),
                        categories = state.options,
                        selectedChipIndex = state.selectedSortChip,
                        onChipSelected = { index ->
                            homeViewModel.onClickSortChip(index)
                        })

                }
            )
        }
    }
}

@Composable
fun TopRatingMoviesList(
    content: @Composable () -> Unit,
    topRatingSeries: List<MovieUiState>,
    listener: HomeInteractionsListener,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = topRatingSeries.isEmpty(), enter = fadeIn(), exit = fadeOut()
    ) {
        Box(
            modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            StateMessage(
                imageDrawable = com.cairosquad.design_system.R.drawable.no_result,
                titleId = com.cairosquad.design_system.R.string.no_results_found,
                descriptionId = com.cairosquad.design_system.R.string.no_results_found_description
            )
        }
    }
    AnimatedVisibility(
        visible = topRatingSeries.isNotEmpty(), enter = fadeIn(), exit = fadeOut()
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {

            item {
                Column {
                    content()
                }
            }
            item {
                LazyVerticalGrid(
                    modifier = modifier
                        .heightIn(max = 10000.dp)
                        .fillMaxWidth(),
                    columns = GridCells.Adaptive(minSize = 101.33.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    userScrollEnabled = false
                ) {
                    items(topRatingSeries) { moreRecommended ->
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
    }
}