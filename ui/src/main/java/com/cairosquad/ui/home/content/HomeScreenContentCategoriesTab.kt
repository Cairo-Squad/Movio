import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.cairosquad.design_system.theme.Theme
import com.cairosquad.ui.R
import com.cairosquad.ui.movio_component.CategoriesChips
import com.cairosquad.ui.movio_component.MovieCard
import com.cairosquad.viewmodel.home.HomeInteractionsListener
import com.cairosquad.viewmodel.home.HomeScreenState

@Composable
fun HomeScreenContentCategoriesTab(
    screenState: HomeScreenState,
    listener: HomeInteractionsListener,
    modifier: Modifier = Modifier
) {
    val media = screenState.categoriesMedia.collectAsLazyPagingItems()
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .align(Alignment.TopEnd)
                .size(230.dp)
                .blur(263.85.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                .background(Theme.color.surfaces.onSurfaceAt5, shape = CircleShape)
        )

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            columns = GridCells.Adaptive(minSize = 101.33.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    CategoriesChips(
                        categories = listOf(stringResource(R.string.genre_all)) +
                                screenState.genres.drop(1).map { it.name },
                        selectedChipIndex = screenState.selectedGenreIndex,
                        onChipSelected = { index ->
                            listener.onGenreSelected(index)
                        }
                    )
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    CategoriesChips(
                        categories = HomeScreenState.SortingType.entries.map { stringResource(it.titleId) },
                        selectedChipIndex = screenState.selectedSortingType.ordinal,
                        onChipSelected = { index ->
                            listener.onSortingSelected(HomeScreenState.SortingType.entries[index])
                        }
                    )
                }
            }

            items(media.itemCount) { index ->
                media[index]?.let { moreRecommended ->
                    MovieCard(
                        modifier = Modifier.clickable {
                            listener.onClickMedia(moreRecommended.id, moreRecommended.isMovie)
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